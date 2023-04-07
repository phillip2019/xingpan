package org.jeecg.modules.ma.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLEncoder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.facebook.presto.jdbc.internal.okhttp3.internal.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.JacksonBuilder;
import org.jeecg.modules.cg.entity.CgShopsZf;
import org.jeecg.modules.cg.service.ICgShopsZfService;
import org.jeecg.modules.ma.entity.*;
import org.jeecg.modules.ma.exception.*;
import org.jeecg.modules.ma.mapper.*;
import org.jeecg.modules.ma.mapper.dto.MaMarketFloorNumDTO;
import org.jeecg.modules.ma.service.IMaActiveService;
import org.jeecg.modules.ma.service.IMaPositionShopService;
import org.jeecg.modules.ma.service.IMaTaiKaShopService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Autowired
    private IMaPositionShopService positionShopService;

    @Autowired
    private IMaTaiKaShopService taiKaShopService;

    @Autowired
    private ICgShopsZfService shopsZfService;

    @Autowired
    private MaTaiKaShopMapper taikaShopMapper;

    @Value("${cg.ylbIdHost}")
    private String saveYlbIdHost;

    @Value("${cg.accessTokenHost}")
    private String accessTokenHost;

    public static final ConnectionSpec TLS_SPEC = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_0, TlsVersion.TLS_1_1, TlsVersion.TLS_1_2)
            .build();

    public static final OkHttpClient okHttpClient =  new OkHttpClient.Builder()
            .dispatcher(new Dispatcher(Executors.newFixedThreadPool(3)))
            .connectionPool(new ConnectionPool(1, 60000, TimeUnit.MILLISECONDS))
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .connectTimeout(60000, TimeUnit.MILLISECONDS)
            .writeTimeout(60000, TimeUnit.MILLISECONDS)
            .protocols(Util.immutableList(Protocol.HTTP_1_1))
            .connectionSpecs(ImmutableList.of(TLS_SPEC, ConnectionSpec.CLEARTEXT))
            .build()
            ;


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
                log.info("导入活动编号: {}, 店铺数量: {}", activeId, list.size());
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
            // 过滤脏数据
            if (Objects.isNull(ylbMaterial.getShopId())) {
                continue;
            }
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
                    .setStatus(1)
            ;

            MaPositionAddress positionAddress = new MaPositionAddress();
            positionAddress.setPositionNo(positionNo)
                    .setMarketName(marketName)
                    .setFloor(floor)
                    .setPositionAddressType(positionAddressType)
                    .setPositionAddressDetail(positionAddressDetail)
                    .setIndustryName(industryName)
                    .setUsedStatus(1)
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
        log.info("预检查活动: {}, 易拉宝excel", activeId);
        List<MaPosition> positionList = preCheckImportYlbExcel(activeId, maActiveYlbMaterialList);

        log.info("活动: {}, 生成易拉宝微信公众号带参二维码和易拉宝店铺微信公众号带参二维码， 点位数量为: {}", activeId, positionList.size());
        generateWeChatOfficialQrCode(positionList);

        log.info("活动: {}, 保存点位、点位地址和点位店铺信息", activeId);
        List<MaPositionShop> preSavePositionShopList = new ArrayList<>(1000);
        for (MaPosition maPositionDTO : positionList) {
            // 提交生成易拉宝点位信息
            positionMapper.insertPositionReturnId(maPositionDTO);
            // 生成易拉宝点位店铺
            maPositionDTO.getPositionAddress().setPositionId(maPositionDTO.getId());
            positionAddressMapper.insert(maPositionDTO.getPositionAddress());

            for (MaPositionShop positionShop : maPositionDTO.getPositionShopList()) {
                positionShop.setPositionId(maPositionDTO.getId());
            }
            preSavePositionShopList.addAll(maPositionDTO.getPositionShopList());
            if (preSavePositionShopList.size() >= 996) {
                positionShopService.saveBatch(preSavePositionShopList);
                preSavePositionShopList.clear();
            }
        }
        if (preSavePositionShopList.size() > 0) {
            positionShopService.saveBatch(preSavePositionShopList);
        }
    }

    private String getWeChatOfficialAccessToken() throws IOException, CWxAccessTokenException {
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url(accessTokenHost + "/user/bindwechatofficial/getofficialaccesstoken")
                .method("GET", null)
                .build();
        Response response = okHttpClient.newCall(request).execute();
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
        ylbObjectNode.put("position_no", positionEntry.getPositionNo())
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
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, JacksonBuilder.MAPPER.writeValueAsString(dataObjectNode));
        Request request = new Request.Builder()
                .url(String.format("%s/ums/ylb/add", saveYlbIdHost))
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = okHttpClient.newCall(request).execute();
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
    private void getWeChatOfficialQrCode(String accessToken, String shopId, Long ylbId, MaPosition positionEntry, MaPositionShop positionShop) throws IOException, CWxQrCodeException {
        MediaType mediaType = MediaType.parse("application/json");
        ObjectNode paramsObjectNode = JacksonBuilder.MAPPER.createObjectNode();
        paramsObjectNode.put("action_name", "QR_LIMIT_STR_SCENE");
        ObjectNode actionInfoObjectNode = JacksonBuilder.MAPPER.createObjectNode();
        paramsObjectNode.set("action_info", actionInfoObjectNode);
        ObjectNode sceneObjectNode = JacksonBuilder.MAPPER.createObjectNode();
        sceneObjectNode.put("scene_str", "YLBId: " + ylbId);

        actionInfoObjectNode.set("scene", sceneObjectNode);
        RequestBody body = RequestBody.create(mediaType, JacksonBuilder.MAPPER.writeValueAsString(paramsObjectNode));
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        //第四步 call对象调用enqueue()方法，通过Callback()回调拿到响应体Response
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //异步请求失败之后的回调
                log.error("获取微信公众号带参二维码失败: {}", ylbId, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //异步请求成功之后的回调
                String responseContent = Objects.requireNonNull(response.body()).string();
                JsonNode jsonNode = JacksonBuilder.MAPPER.readTree(responseContent);
                if (jsonNode.has("errcode")) {
                    log.error("获取带参微信公众号二维码失败，返回内容为: {}", responseContent);
                    return;
                }
                String ticket = jsonNode.get("ticket").asText();
                String url = jsonNode.get("url").asText();
                if (positionShop != null) {
                    positionShop.setQrCodeTicket(ticket);
                    positionShop.setQrCodeUrl(String.format("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s", URLEncoder.createDefault().encode(ticket, StandardCharsets.UTF_8)));
                    positionShop.setUrl(url);
                }

                if (positionEntry != null) {
                    positionEntry.setQrCodeTicket(ticket);
                    positionEntry.setQrCodeUrl(String.format("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s", URLEncoder.createDefault().encode(ticket, StandardCharsets.UTF_8)));
                    positionEntry.setUrl(url);
                }
            }
        });
    }

    /**
     * 生成微信公众号二维码
     **/
    private void generateWeChatOfficialQrCode(List<MaPosition> positionList) throws Exception {
        log.info("开始生成微信公众号带参二维码...");
        Long ts = System.currentTimeMillis() / 1000;
        // 获取当前可用access_token
        String accessToken;
        List<Long> ylbIdList = new ArrayList<>(positionList.size());
        Long ylbParamId;
        int pos = 0;
        int maxBatchSize = positionList.size();
        for (MaPosition positionEntry : positionList) {
            pos++;
            log.info("开始生成第: {}, 总计: {}, 市场： {}， 楼层: {}， 易拉宝编号: {}, 易拉宝序号: {}, 带参二维码", pos, maxBatchSize, positionEntry.getMarketName(), positionEntry.getFloor(), positionEntry.getPositionNo(), positionEntry.getSeqNo());
            accessToken = getWeChatOfficialAccessToken();
            ylbParamId = saveYlbId2DB(positionEntry, ts);
            ylbIdList.add(ylbParamId);
            getWeChatOfficialQrCode(accessToken, null, ylbParamId, positionEntry, null);

            for (MaPositionShop positionShop : positionEntry.getPositionShopList()) {
                Thread.sleep(100);
                ylbParamId = saveYlbId2DB(positionEntry, positionShop, ts);
                ylbIdList.add(ylbParamId);
                getWeChatOfficialQrCode(accessToken, positionShop.getShopId(), ylbParamId, null, positionShop);
            }
        }
        Thread.sleep(3 * 1000);
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

    @Override
    public void downloadActiveQrCode(Long activeId, String srcSource) throws IOException {
        log.info("开始查询活动: {}对应易拉宝带参微信二维码信息", activeId);
        List<MaActiveYlbQrCodeUrl> activeYlbQrCodeUrlList = positionMapper.selectYlbQrCodeByActiveId(activeId);
        // 按照序号分组，同一个分组的存储到同一个列表中
        Map<String, List<MaActiveYlbQrCodeUrl>> seqNoYlbQrCodeListMap = new HashMap<>(activeYlbQrCodeUrlList.size() / 5);
        for (MaActiveYlbQrCodeUrl activeYlbQrCodeUrl : activeYlbQrCodeUrlList) {
            if (!seqNoYlbQrCodeListMap.containsKey(activeYlbQrCodeUrl.getSeqNo())) {
                seqNoYlbQrCodeListMap.put(activeYlbQrCodeUrl.getSeqNo(), new ArrayList<>(4));
            }
            List<MaActiveYlbQrCodeUrl> seqNoYlbQrCodeList = seqNoYlbQrCodeListMap.get(activeYlbQrCodeUrl.getSeqNo());
            seqNoYlbQrCodeList.add(activeYlbQrCodeUrl);
        }
        String finalActiveDirPath = Paths.get(srcSource, String.valueOf(activeId)).toString();
        FileUtil.del(finalActiveDirPath);
        FileUtil.mkdir(finalActiveDirPath);
        log.info("活动: {}, 开始下载易拉宝带参微信二维码图片， 总计:{}张易拉宝", activeId, seqNoYlbQrCodeListMap.size());
        int pos = 0;
        for (Map.Entry<String, List<MaActiveYlbQrCodeUrl>> seqNoYlbQrCodeEntry : seqNoYlbQrCodeListMap.entrySet()) {
            pos++;
            String seqNo = seqNoYlbQrCodeEntry.getKey();
            List<MaActiveYlbQrCodeUrl> ylbQrCodeUrlList = seqNoYlbQrCodeEntry.getValue();
            // 存储易拉宝编号图片
            MaActiveYlbQrCodeUrl maActiveYlbQrCodeUrl = ylbQrCodeUrlList.get(0);
            String marketName = maActiveYlbQrCodeUrl.getMarketName();
            String floor = maActiveYlbQrCodeUrl.getFloor();
            String ylbQrCodeUrl = maActiveYlbQrCodeUrl.getYlbQrCodeUrl();
            log.info("活动: {}, 开始下载易拉宝带参微信二维码图片， 当前第: {}张易拉宝, 总计:{}张易拉宝,", activeId, pos, seqNoYlbQrCodeListMap.size());
            // 易拉宝图片存储路径为： 目的目录+市场+楼层目录
            Path ylbSaveDirPath = Paths.get(finalActiveDirPath, marketName, floor);
            String ylbSaveDir = ylbSaveDirPath.toString();
            if (!FileUtil.exist(ylbSaveDirPath.toFile())) {
                FileUtil.mkdir(ylbSaveDirPath.toFile());
            }
            FileUtils.copyURLToFile(new URL(ylbQrCodeUrl), Paths.get(ylbSaveDir, seqNo + ".png").toFile());

            // 下载易拉宝店铺二维码图片
            String ylbShopQrCodeUrl;
            for (MaActiveYlbQrCodeUrl qrCodeUrl : ylbQrCodeUrlList) {
                ylbShopQrCodeUrl = qrCodeUrl.getYlbShopQrCodeUrl();
                FileUtils.copyURLToFile(new URL(ylbShopQrCodeUrl), Paths.get(ylbSaveDir, seqNo + '-' + qrCodeUrl.getShopId() + ".png").toFile());
            }
        }
    }

    /**
     * 导入商铺台卡物料数据
     *
     * @param activeId 活动编号
     * @param request  请求体
     * @param response 返回体
     * @param clazz    商铺台卡前端物料表
     * @return 导入消息
     */
    @Override
    public Result<?> importTaiKaExcel(Long activeId, HttpServletRequest request, HttpServletResponse response, Class<MaActiveTaiKaMaterial> clazz) {
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
                List<MaActiveTaiKaMaterial> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                log.info("导入活动编号: {}, 店铺数量: {}", activeId, list.size());
                //update-begin-author:taoyan date:20190528 for:批量插入数据
                long start = System.currentTimeMillis();
                saveTaiKaBatch(activeId, list);
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
     * @description 基于活动编号，检查输入商铺台卡店铺物料，生成台卡店铺二维码，返回商铺台卡店铺二维码列表
     * @author xiaowei.song
     * @date 2023/4/4 10:15
     * @version v1.0.0
     **/
    private List<MaTaiKaShop> preCheckImportTaiKaExcel(Long activeId, List<MaActiveTaiKaMaterial> maActiveTaiKaMaterialList) throws CRequestParamException {
        // 检查活动编号是否存在，若不存在抛出异常
        MaActive maActive = getById(activeId);
        if (maActive == null) {
            throw new CRequestParamException(String.format("活动编号: %s, 不存在，请先选中活动，再导入商铺台卡店铺物料！", activeId));
        }
        // 得到需要处理的shopIds
        List<String> shopIdsFE =   maActiveTaiKaMaterialList.stream().filter(Objects::nonNull).filter(e -> StringUtils.isNotBlank(e.getShopId())).map(MaActiveTaiKaMaterial::getShopId).collect(Collectors.toList());
        if (shopIdsFE.size() == 0) {
            throw new CRequestParamException(String.format("活动编号: %s, 导入商铺台卡店铺物料为空，请稍后再试！", activeId));
        }
        // 通过shopId去查询店铺维表详情信息
        List<CgShopsZf>  shopsZfList = shopsZfService.queryListByIds(shopIdsFE);

        Set<String> shopIdsDb = shopsZfList.stream().map(CgShopsZf::getShopId).collect(Collectors.toSet());
        Set<String> shopIdsFESet = new TreeSet<>(shopIdsFE);
        shopIdsFESet.removeAll(shopIdsDb);
        if (shopIdsFESet.size() > 0) {
            throw new CRequestParamException(String.format("活动编号: %s, 不存在，店铺物料列表: [%s]不存在或已关店！", activeId, shopIdsFESet));
        }
        List<MaTaiKaShop> taiKaShopList = new ArrayList<>(maActiveTaiKaMaterialList.size());
        MaTaiKaShop taiKaShop;
        for (CgShopsZf shopsZf : shopsZfList) {
            taiKaShop = new MaTaiKaShop();
            taiKaShop.setActiveId(activeId)
                    .setBoothIdArr(shopsZf.getBoothIdArr())
                    .setBoothNoArr(shopsZf.getBoothNo())
                    .setMarketName(shopsZf.getMarketName())
                    .setShopId(shopsZf.getShopId())
                    .setShopName(shopsZf.getShopName())
                    .setStatus(1)
            ;
            taiKaShopList.add(taiKaShop);
        }
        return taiKaShopList;
    }

    private Long saveTaiKaId2DB(MaTaiKaShop taiKaShop,  Long ts) throws IOException, CSaveTaiKa2DBException {
        ObjectNode taiKaObjectNode = JacksonBuilder.MAPPER.createObjectNode();
        taiKaObjectNode.put("active_id", taiKaShop.getActiveId())
                .put("qr_code_type", "static")
                .put("ts", ts)
                .put("user_type", "buyer")
                .put("shop_id", taiKaShop.getShopId())
                .put("shop_name", taiKaShop.getShopName())
                .put("market_name", taiKaShop.getMarketName())
                .put("booth_id_arr", taiKaShop.getBoothIdArr())
                .put("booth_no_arr", taiKaShop.getBoothNoArr())
        ;

        ObjectNode dataObjectNode = JacksonBuilder.MAPPER.createObjectNode();
        dataObjectNode.put("data", JacksonBuilder.MAPPER.writeValueAsString(taiKaObjectNode));
        dataObjectNode.put("source", "TAIKA");
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, JacksonBuilder.MAPPER.writeValueAsString(dataObjectNode));
        Request request = new Request.Builder()
                .url(String.format("%s/ums/ylb/add", saveYlbIdHost))
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseContent = response.body().string();
        JsonNode jsonNode = JacksonBuilder.MAPPER.readTree(responseContent);
        if (jsonNode.get("code").asInt(0) != 20000) {
            throw new CSaveTaiKa2DBException(String.format("保存商铺台卡店铺参数失败，返回内容为: %s", responseContent));
        }
        return jsonNode.get("data").asLong();
    }

    /**
     * 获取对应商铺台卡店铺微信公众号带参二维码
     * @param accessToken 微信公众号授权二维码
     * @param taiKaShop 台卡店铺实例
     **/
    private void getTaiKaWeChatOfficialQrCode(String accessToken, MaTaiKaShop taiKaShop) throws IOException, CWxQrCodeException {
        MediaType mediaType = MediaType.parse("application/json");
        String taiKaId = taiKaShop.getTaiKaId();
        String shopId = taiKaShop.getShopId();

        ObjectNode paramsObjectNode = JacksonBuilder.MAPPER.createObjectNode();
        paramsObjectNode.put("action_name", "QR_LIMIT_STR_SCENE");
        ObjectNode actionInfoObjectNode = JacksonBuilder.MAPPER.createObjectNode();
        paramsObjectNode.set("action_info", actionInfoObjectNode);
        ObjectNode sceneObjectNode = JacksonBuilder.MAPPER.createObjectNode();
        sceneObjectNode.put("scene_str", "TAIKA_ID: " + taiKaId);

        actionInfoObjectNode.set("scene", sceneObjectNode);
        RequestBody body = RequestBody.create(mediaType, JacksonBuilder.MAPPER.writeValueAsString(paramsObjectNode));
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        //第四步 call对象调用enqueue()方法，通过Callback()回调拿到响应体Response
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //异步请求失败之后的回调
                log.error("获取微信公众号带参二维码失败: {}", taiKaId, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //异步请求成功之后的回调
                String responseContent = Objects.requireNonNull(response.body()).string();
                JsonNode jsonNode = JacksonBuilder.MAPPER.readTree(responseContent);
                if (jsonNode.has("errcode")) {
                    log.error("获取带参微信公众号二维码失败，返回内容为: {}", responseContent);
                    return;
                }
                String ticket = jsonNode.get("ticket").asText();
                String url = jsonNode.get("url").asText();
                taiKaShop.setQrCodeTicket(ticket);
                taiKaShop.setQrCodeUrl(String.format("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s", URLEncoder.createDefault().encode(ticket, StandardCharsets.UTF_8)));
                taiKaShop.setUrl(url);
            }
        });
    }


    /**
     * 生成台卡微信公众号二维码
     **/
    private void generateTaiKaWeChatOfficialQrCode(List<MaTaiKaShop> taiKaList) throws Exception {
        log.info("开始生成微信公众号带参二维码...");
        Long ts = System.currentTimeMillis() / 1000;
        // 获取当前可用access_token
        String accessToken = getWeChatOfficialAccessToken();;
        List<Long> taiKaIdList = new ArrayList<>(taiKaList.size());
        Long taiKaParamId;
        int pos = 0;
        int maxBatchSize = taiKaList.size();
        for (MaTaiKaShop taiKaShop : taiKaList) {
            pos++;
            log.info("开始生成第: {}, 总计: {}, 市场: {}, 店铺: {}， 店铺名: {}, 带参二维码", pos, maxBatchSize, taiKaShop.getMarketName(), taiKaShop.getShopId(), taiKaShop.getShopName());
            taiKaParamId = saveTaiKaId2DB(taiKaShop, ts);
            taiKaIdList.add(taiKaParamId);
            taiKaShop.setTaiKaId(String.valueOf(taiKaParamId));
            getTaiKaWeChatOfficialQrCode(accessToken, taiKaShop);
            if (pos % 50 == 0) {
                Thread.sleep(10 * 1000);
                accessToken = getWeChatOfficialAccessToken();
            }
        }
        // TODO 若执行失败，则删除生成的参数编号
    }

    /**
     * @description 基于活动编号，输入商铺台卡物料，生成商铺台卡店铺点位
     * @author xiaowei.song
     * @date 2023/04/04 10:15
     * @version v1.0.0
     **/
    @Transactional(rollbackFor = Exception.class)
    public void saveTaiKaBatch(Long activeId, List<MaActiveTaiKaMaterial> maActiveTaiKaMaterialList) throws Exception {
        log.info("预检查活动: {}, 商铺台卡excel", activeId);
        List<MaTaiKaShop> taiKaList = preCheckImportTaiKaExcel(activeId, maActiveTaiKaMaterialList);

        log.info("活动: {}, 生成商铺台卡店铺微信公众号带参二维码， 店铺数量为: {}", activeId, taiKaList.size());
        generateTaiKaWeChatOfficialQrCode(taiKaList);

        log.info("活动: {}, 保存商铺台卡店铺信息", activeId);
        taiKaShopService.saveBatch(taiKaList);
    }

    @Override
    public void downloadActiveTaiKaQrCode(Long activeId, String srcSource) throws IOException {
        log.info("开始查询活动: {}对应台卡带参微信二维码信息", activeId);
        List<MaActiveTaiKaQrCodeUrl> activeTaiKaQrCodeUrlList = taikaShopMapper.selectTaiKaQrCodeByActiveId(activeId);
        // 下载活动台卡店铺微信二维码

        String finalActiveDirPath = Paths.get(srcSource, String.valueOf(activeId)).toString();
        FileUtil.del(finalActiveDirPath);
        FileUtil.mkdir(finalActiveDirPath);
        log.info("活动: {}, 开始下载台卡店铺带参微信二维码图片， 总计: {}张图片", activeId, activeTaiKaQrCodeUrlList.size());
        int pos = 0;
        for (MaActiveTaiKaQrCodeUrl taiKaQrCodeUrl : activeTaiKaQrCodeUrlList) {
            pos++;
            String marketName = taiKaQrCodeUrl.getMarketName();
            String shopId = taiKaQrCodeUrl.getShopId();
            String shopName = taiKaQrCodeUrl.getShopName();
            String qrCodeUrl = taiKaQrCodeUrl.getTaiKaShopQrCodeUrl();
            log.info("活动: {}, 开始下载台卡店铺带参微信二维码图片， 当前第: {}张二维码, 总计: {}张二维码,", activeId, pos, activeTaiKaQrCodeUrlList.size());
            // 图片存储路径为： 目的目录+市场
            Path taiKaSaveDirPath = Paths.get(finalActiveDirPath, marketName);
            String taiKaSaveDir = taiKaSaveDirPath.toString();
            if (!FileUtil.exist(taiKaSaveDirPath.toFile())) {
                FileUtil.mkdir(taiKaSaveDirPath.toFile());
            }
            FileUtils.copyURLToFile(new URL(qrCodeUrl), Paths.get(taiKaSaveDir, shopId + "_" + shopName + ".png").toFile());
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        ObjectNode ylbIdObjectNode = JacksonBuilder.MAPPER.createObjectNode();
        ylbIdObjectNode.put("shop_id", "432432");
        ylbIdObjectNode.put("id", 23432);
        ylbIdObjectNode.put("type", "易拉宝");
        System.out.println(JacksonBuilder.MAPPER.writeValueAsString(ylbIdObjectNode));
    }
}
