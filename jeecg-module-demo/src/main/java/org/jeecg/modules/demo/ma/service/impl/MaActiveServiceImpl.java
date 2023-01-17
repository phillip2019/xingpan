package org.jeecg.modules.demo.ma.service.impl;

import cn.hutool.core.net.URLEncoder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.JacksonBuilder;
import org.jeecg.modules.demo.ma.entity.*;
import org.jeecg.modules.demo.ma.exception.CRequestParamException;
import org.jeecg.modules.demo.ma.exception.CSaveYlb2DBException;
import org.jeecg.modules.demo.ma.exception.CWxAccessTokenException;
import org.jeecg.modules.demo.ma.exception.CWxQrCodeException;
import org.jeecg.modules.demo.ma.mapper.MaActiveMapper;
import org.jeecg.modules.demo.ma.mapper.MaPositionAddressMapper;
import org.jeecg.modules.demo.ma.mapper.MaPositionMapper;
import org.jeecg.modules.demo.ma.mapper.MaPositionShopMapper;
import org.jeecg.modules.demo.ma.mapper.dto.MaMarketFloorNumDTO;
import org.jeecg.modules.demo.ma.service.IMaActiveService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Description: 活动
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
@Slf4j
@Service
public class MaActiveServiceImpl extends ServiceImpl<MaActiveMapper, MaActive> implements IMaActiveService {

    @Autowired
    private MaPositionMapper positionMapper;

    @Autowired
    private MaPositionAddressMapper positionAddressMapper;

    @Autowired
    private MaPositionShopMapper positionShopMapper;

    public static final Map<String, String> MARKET_NAME2TAG_NAME_MAP = ImmutableMap.<String, String>builder()
            .put("国际商贸城一区", "M01")
            .put("国际商贸城一区东扩", "M11")
            .put("国际商贸城二区", "M02")
            .put("国际商贸城三区", "M03")
            .put("国际商贸城四区", "M04")
            .put("国际商贸城五区", "M05")
            .build();

    public static final Map<String, String> FLOOR_NAME2TAG_NAME_MAP = ImmutableMap.<String, String>builder()
            .put("一楼", "F1")
            .put("二楼", "F2")
            .put("三楼", "F3")
            .put("四楼", "F4")
            .put("五楼", "F5")
            .put("六楼", "F6")
            .build();

    /**
     * TODO 解析易拉宝物料数据
     * TODO 生成带参数的微信公众号二维码
     * @description 解析易拉宝物料数据
     * @author xiaowei.song
     * @date 2023/1/17 9:00
     * @version v1.0.0
     **/
    @Override
    public Result<?> importYlbExcel(Long activeId, HttpServletRequest request, HttpServletResponse response, Class<MaActiveYlbMaterial> clazz) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<MaActiveYlbMaterial> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                //update-begin-author:taoyan date:20190528 for:批量插入数据
                long start = System.currentTimeMillis();
                saveYlbBatch(activeId, list);
                //400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
                //1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                //update-end-author:taoyan date:20190528 for:批量插入数据
                return Result.ok("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                //update-begin-author:taoyan date:20211124 for: 导入数据重复增加提示
                String msg = e.getMessage();
                log.error(msg, e);
                if(msg != null && msg.contains("Duplicate entry")){
                    return Result.error("文件导入失败:有重复数据！");
                } else {
                    return Result.error("文件导入失败:" + e.getMessage());
                }
                //update-end-author:taoyan date:20211124 for: 导入数据重复增加提示
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }


    /**
     * @description 基于活动编号，检查输入易拉宝物料，生成易拉宝点位、易拉宝点位店铺、点位地址信息，返回易拉宝点位列表
     * @author xiaowei.song
     * @date 2023/1/17 10:15
     * @version v1.0.0
     **/
    private List<MaPosition> preCheckImportYlbExcel(Long activeId, List<MaActiveYlbMaterial> maActiveYlbMaterialList) throws CRequestParamException {
        // 检查活动编号是否存在，若不存在抛出异常
        MaActive maActive = getById(activeId);
        if (maActive == null) {
            throw new CRequestParamException(String.format("活动编号: %s, 不存在，请先选中活动，再导入易拉宝物料！", activeId));
        }

        // 按照序号分组，生成易拉宝分组
        Map<String, List<MaActiveYlbMaterial>> ylbMaterialMapList = new HashMap<>(maActiveYlbMaterialList.size() / 4);
        String seqNo = null;
        for (MaActiveYlbMaterial ylbMaterial : maActiveYlbMaterialList) {
            if (StringUtils.isNotBlank(ylbMaterial.getSeqNo())) {
                seqNo = ylbMaterial.getSeqNo();
            } else {
                ylbMaterial.setSeqNo(seqNo);
            }
            if (!ylbMaterialMapList.containsKey(seqNo)) {
                ylbMaterialMapList.put(seqNo, new ArrayList<>(4));
            }
            List<MaActiveYlbMaterial> ylbMaterialList = ylbMaterialMapList.get(ylbMaterial.getSeqNo());
            ylbMaterialList.add(ylbMaterial);
        }

        // TODO 校验统一分组下市场和楼层是否一样，必须一致，才可以赋予同一序号
        // TODO 校验市场和楼层必须在选定访问内，不可以超出
        // TODO 校验管理员账号和姓名是否一样

        // 初始化当前活动市场，楼层部署易拉宝情况
        Map<String, Map<String, Integer>> marketFloorSeqIdMap = initMarketFloorPosition(activeId);
        List<MaPosition> positionList = new ArrayList<>(ylbMaterialMapList.size());
        MaPosition maPosition = null;
        // 添加易拉宝编号
        for (Map.Entry<String, List<MaActiveYlbMaterial>> integerListEntry : ylbMaterialMapList.entrySet()) {
            maPosition = new MaPosition();
            maPosition.setCreateBy(((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername());
            maPosition.setCreateTime(new Date());
            positionList.add(maPosition);
            seqNo = integerListEntry.getKey();
            List<MaActiveYlbMaterial> ylbMaterialList = integerListEntry.getValue();
            if (ylbMaterialList.size() == 0) {
                continue;
            }

            MaActiveYlbMaterial ylbMaterial = ylbMaterialList.get(0);
            String marketName = ylbMaterial.getMarketName();
            String floor = ylbMaterial.getFloor();

            String ownerAccount = ylbMaterial.getOwnerAccount();
            String ownerName = ylbMaterial.getOwnerName();
            String positionAddressType = ylbMaterial.getPositionAddressType();
            String positionAddressDetail = ylbMaterial.getPositionAddressDetail();
            String industryName = ylbMaterial.getIndustryName();
            if (!marketFloorSeqIdMap.containsKey(marketName)) {
                marketFloorSeqIdMap.put(marketName, new HashMap<String, Integer>(){
                    {
                        put(floor, 0);
                    }});
            }

            Map<String, Integer> floorNumM = marketFloorSeqIdMap.get(marketName);
            if (!floorNumM.containsKey(floor)) {
                floorNumM.put(floor, 0);
            }
            Integer marketFloorPositionNum = floorNumM.get(floor) + 1;
            floorNumM.put(floor, marketFloorPositionNum);

            // *******生成易拉宝编号**************************************************
            //        M market
            //        F floor
            //        标识这个易拉宝所属活动，应该放在哪个市场，哪个楼层，可以全楼层到处挪动
            // **********************************************************************
            // 生成易拉宝点位, 当前市场，楼层分组，保留2位
            String positionNo = String.format("%s%s%02d", MARKET_NAME2TAG_NAME_MAP.get(marketName), FLOOR_NAME2TAG_NAME_MAP.get(floor), marketFloorPositionNum);

            maPosition.setPositionNo(positionNo)
                    .setPositionType("易拉宝")
                    .setSeqNo(seqNo)
                    .setActiveId(activeId)
                    .setMarketName(marketName)
                    .setFloor(floor)
                    .setOwnerAccount(ownerAccount)
                    .setOwnerName(ownerName)
                    .setStatus("启用")
            ;

            MaPositionAddress positionAddress = new MaPositionAddress();
            positionAddress.setPositionNo(positionNo)
                    .setMarketName(marketName)
                    .setFloor(floor)
                    .setPositionAddressType(positionAddressType)
                    .setPositionAddressDetail(positionAddressDetail)
                    .setIndustryName(industryName)
                    .setUsedStatus("启用")
            ;
            maPosition.setPositionAddress(positionAddress);

            List<MaPositionShop> positionShopList = maPosition.getPositionShopList();
            MaPositionShop positionShop;
            // 拼装店铺信息
            for (MaActiveYlbMaterial material : ylbMaterialList) {
                positionShop = new MaPositionShop();
                positionShopList.add(positionShop);

                positionShop.setPositionNo(positionNo)
                        .setShopId(material.getShopId())
                        .setShopName(material.getShopName())
                        .setShopCover(material.getShopCover())
                        .setMainGoodsArr(material.getMainGoodsArr())
                        .setMainBusinessScope(material.getMainBusinessScope())
                        .setShopLocation(material.getShopLocation())
                        .setShopIntroduction(material.getShopIntroduction())
                ;
            }
        }
        return positionList;
    }
    /**
     * @description 基于活动编号，输入易拉宝物料，生成易拉宝点位、易拉宝点位店铺、点位地址信息
     * @author xiaowei.song
     * @date 2023/1/17 10:15
     * @version v1.0.0       
     **/
    @Transactional(rollbackFor = Exception.class)
    public void saveYlbBatch(Long activeId, List<MaActiveYlbMaterial> maActiveYlbMaterialList) throws Exception {
        List<MaPosition> positionList = preCheckImportYlbExcel(activeId, maActiveYlbMaterialList);

        // TODO 生成企业微信公众号二维码信息
        generateWeChatOfficialQrCode(positionList);

        for (MaPosition maPositionDTO : positionList) {
            // 提交生成易拉宝点位信息
            positionMapper.insertPositionReturnId(maPositionDTO);
            // 生成易拉宝点位店铺
            maPositionDTO.getPositionAddress().setPositionId(maPositionDTO.getId());
            positionAddressMapper.insert(maPositionDTO.getPositionAddress());

            // TODO 后期优化，变更成批量插入模式
            for (MaPositionShop positionShop : maPositionDTO.getPositionShopList()) {
                positionShop.setPositionId(maPositionDTO.getId());
                // 生成易拉宝点位地址
                positionShopMapper.insert(positionShop);
            }
        }
    }

    private String getWeChatOfficialAccessToken() throws IOException, CWxAccessTokenException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url("https://user.chinagoods.com/user/bindwechatofficial/getofficialaccesstoken")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        String responseContent = response.body().string();
        JsonNode jsonNode = JacksonBuilder.MAPPER.readTree(responseContent);
        if (!jsonNode.get("success").asBoolean()) {
            throw new CWxAccessTokenException(String.format("获取access_token失败，返回内容为: %s", responseContent));
        }
        return jsonNode.get("data").asText();
    }

    private Long saveYlbId2DB(MaPosition positionEntry, Long ts) throws IOException, CSaveYlb2DBException {
        return saveYlbId2DB(positionEntry, null, ts);
    }

    private Long saveYlbId2DB(MaPosition positionEntry, MaPositionShop positionShop, Long ts) throws IOException, CSaveYlb2DBException {
//        + 点位编号 position_no
//                + 市场  market_name
//                + ~~楼层~~  floor
//                + ~~位置类型~~   position_address_type:
//        + ~~具体位置~~   position_address_detail
//                + ~~行业~~ industry_name
//                + 管理员账号 owner_account
//                + 管理员名字 owner_name
//
//                + 店铺编号 shop_id
//                + 店铺名称 shop_name
//                + 二维码类型 qcode_type static
//        + 时间戳 ts 当前10位时间戳
//        + 人员角色 user_type buyer(采购商)|supplier(经营户)|owner(管理员)

        ObjectNode ylbObjectNode = JacksonBuilder.MAPPER.createObjectNode();
        ylbObjectNode.put("position_no", positionEntry.getId())
                .put("active_id", positionEntry.getActiveId())
                .put("owner_account", positionEntry.getOwnerAccount())
                .put("owner_name", positionEntry.getOwnerName())
                .put("qr_code_type", "static")
                .put("ts", ts)
                .put("user_type", "buyer")
        ;

        if (positionShop != null) {
            ylbObjectNode.put("shop_id", positionShop.getShopId())
                    .put("shop_name", positionShop.getShopName())
                    ;
        }

        ObjectNode dataObjectNode = JacksonBuilder.MAPPER.createObjectNode();
        dataObjectNode.put("data", JacksonBuilder.MAPPER.writeValueAsString(ylbObjectNode));
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, JacksonBuilder.MAPPER.writeValueAsString(dataObjectNode));
        Request request = new Request.Builder()
                .url("https://testwhale.chinagoods.com/ums/ylb/add")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String responseContent = response.body().string();
        JsonNode jsonNode = JacksonBuilder.MAPPER.readTree(responseContent);
        if (jsonNode.get("code").asInt(0) != 20000) {
            throw new CSaveYlb2DBException(String.format("保存易拉宝参数失败，返回内容为: %s", responseContent));
        }
        return jsonNode.get("data").asLong();
    }


    /**
     * 获取对应店铺，对应易拉宝编号微信公众号带参二维码
     **/
    private String getWeChatOfficialQrCode(String accessToken, String shopId, Long ylbId) throws IOException, CWxQrCodeException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        ObjectNode ylbIdObjectNode = JacksonBuilder.MAPPER.createObjectNode();
        if (StringUtils.isNotBlank(shopId)) {
            ylbIdObjectNode.put("shop_id", shopId);
        }
        ylbIdObjectNode.put("id", ylbId);
        ylbIdObjectNode.put("type", "易拉宝");
        RequestBody body = RequestBody.create(mediaType, "{\"action_name\": \"QR_LIMIT_STR_SCENE\",  \"action_info\": { \"scene\": {  \"scene_str\": \"YLBId: "+ JacksonBuilder.MAPPER.writeValueAsString(ylbIdObjectNode)  + "}}}");
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String responseContent = response.body().string();
        JsonNode jsonNode = JacksonBuilder.MAPPER.readTree(responseContent);
        if (jsonNode.has("errcode")) {
            throw new CWxQrCodeException(String.format("获取带参微信公众号二维码失败，返回内容为: %s", responseContent));
        }
        return jsonNode.get("ticket").asText();
    }

    /**
     * 生成微信公众号二维码
     **/
    private void generateWeChatOfficialQrCode(List<MaPosition> positionList) throws Exception {
        Long ts = System.currentTimeMillis() / 1000;
        // 获取当前可用access_token
        String accessToken;
        List<Long> ylbIdList = new ArrayList<>(positionList.size());
        Long ylbParamId;
        String ylbTicket, shopYlbTicket;
        for (MaPosition positionEntry : positionList) {
            accessToken = getWeChatOfficialAccessToken();
            ylbParamId = saveYlbId2DB(positionEntry, ts);
            ylbIdList.add(ylbParamId);
            ylbTicket = getWeChatOfficialQrCode(accessToken, null, ylbParamId);
            positionEntry.setQrCodeTicket(ylbTicket);
            positionEntry.setQrCodeUrl(String.format("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s", URLEncoder.createDefault().encode(ylbTicket, StandardCharsets.UTF_8)));

            // TODO 生成易拉宝店铺二维码
            for (MaPositionShop positionShop : positionEntry.getPositionShopList()) {
                ylbParamId = saveYlbId2DB(positionEntry, positionShop, ts);
                ylbIdList.add(ylbParamId);
                shopYlbTicket = getWeChatOfficialQrCode(accessToken, positionShop.getShopId(), ylbParamId);
                positionShop.setQrCodeTicket(shopYlbTicket);
                positionShop.setQrCodeUrl(String.format("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s", URLEncoder.createDefault().encode(shopYlbTicket, StandardCharsets.UTF_8)));
            }
        }
        // TODO 若执行失败，则删除生成的参数编号
    }

    /**
     * 获取当前活动市场楼层点位个数
     **/
    private Map<String, Map<String, Integer>> initMarketFloorPosition(Long activeId) {
        List<MaMarketFloorNumDTO> marketFloorNumDTOList = positionMapper.selectMarketFloorPositionNum(activeId);
        Map<String, Map<String, Integer>> curMarketFloorNumMapMap = new HashMap<>(6);

        for (MaMarketFloorNumDTO marketFloorNumDTO : marketFloorNumDTOList) {
            String marketName = marketFloorNumDTO.getMarketName();
            if (!curMarketFloorNumMapMap.containsKey(marketName)) {
                curMarketFloorNumMapMap.put(marketName, new HashMap<>(6));
            }
            Map<String, Integer> floorNumM = curMarketFloorNumMapMap.get(marketName);
            Integer num = marketFloorNumDTO.getNum();
            String floor = marketFloorNumDTO.getFloor();
            floorNumM.put(floor, num);
        }
        return curMarketFloorNumMapMap;
    }
}
