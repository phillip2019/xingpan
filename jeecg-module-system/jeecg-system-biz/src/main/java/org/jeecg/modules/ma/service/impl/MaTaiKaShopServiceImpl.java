package org.jeecg.modules.ma.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.facebook.presto.jdbc.internal.okhttp3.internal.Util;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.JacksonBuilder;
import org.jeecg.modules.ma.entity.MaActiveTaiKaQrCodeUrl;
import org.jeecg.modules.ma.entity.MaTaiKaShop;
import org.jeecg.modules.ma.exception.CSaveTaiKa2DBException;
import org.jeecg.modules.ma.exception.CWxAccessTokenException;
import org.jeecg.modules.ma.exception.CWxQrCodeException;
import org.jeecg.modules.ma.mapper.MaTaiKaShopMapper;
import org.jeecg.modules.ma.service.IMaTaiKaShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Description: ma_tai_ka_shop
 * @Author: jeecg-boot
 * @Date:   2023-04-03
 * @Version: V1.0
 */
@Slf4j
@Service
public class MaTaiKaShopServiceImpl extends ServiceImpl<MaTaiKaShopMapper, MaTaiKaShop> implements IMaTaiKaShopService {

    @Value("${cg.ylbIdHost}")
    private String saveYlbIdHost;

    @Value("${cg.accessTokenHost}")
    private String accessTokenHost;

    @Autowired
    private MaTaiKaShopMapper taikaShopMapper;

    @Resource
    private ISysBaseAPI sysBaseAPI;

    /**
     * The singleton TLS spec.
     **/
    public static final ConnectionSpec TLS_SPEC = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_0, TlsVersion.TLS_1_1, TlsVersion.TLS_1_2)
            .build();

    /**
     * The singleton HTTP client.
     **/
    public final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .dispatcher(new Dispatcher(Executors.newFixedThreadPool(5)))
            .connectionPool(new ConnectionPool(1, 60000, TimeUnit.MILLISECONDS))
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .connectTimeout(60000, TimeUnit.MILLISECONDS)
            .writeTimeout(60000, TimeUnit.MILLISECONDS)
            .protocols(Util.immutableList(Protocol.HTTP_1_1))
            .connectionSpecs(ImmutableList.of(TLS_SPEC, ConnectionSpec.CLEARTEXT))
            .build();


    private String getWeChatOfficialAccessToken() throws IOException, CWxAccessTokenException {
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url(accessTokenHost + "/user/bindwechatofficial/getofficialaccesstoken")
                .method("GET", null)
                .build();
        String responseContent;
        try (Response response = okHttpClient.newCall(request).execute()) {
            assert response.body() != null;
            responseContent = response.body().string();
        }
        JsonNode jsonNode = JacksonBuilder.MAPPER.readTree(responseContent);
        if (!jsonNode.get("success").asBoolean()) {
            throw new CWxAccessTokenException(String.format("获取access_token失败，返回内容为: %s", responseContent));
        }
        return jsonNode.get("data").asText();
    }


    /**
     * 获取对应商铺台卡店铺微信公众号带参二维码
     * @param accessToken 微信公众号授权二维码
     * @param taiKaShop 台卡店铺实例
     **/
    public void getTaiKaWeChatOfficialQrCode(String accessToken, MaTaiKaShop taiKaShop) throws IOException, CWxQrCodeException {
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
        Call call = null;
        Response response = null;
        try {
            call = okHttpClient.newCall(request);
            response = call.execute();
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
        } catch (IOException e) {
            log.error("获取微信公众号带参二维码失败: {}", taiKaId, e);
        } finally {
            if (call != null) {
                call.cancel();
            }
            if (response != null) {
                response.close();
            }
        }

        /*//第四步 call对象调用enqueue()方法，通过Callback()回调拿到响应体Response
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
        });*/
    }


    /**
     * 生成台卡微信公众号二维码
     **/
    public void generateTaiKaWeChatOfficialQrCode(List<MaTaiKaShop> taiKaList) throws Exception {
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
//                Thread.sleep(3 * 1000);
                accessToken = getWeChatOfficialAccessToken();
            }
        }
        // TODO 若执行失败，则删除生成的参数编号
    }


    public Long saveTaiKaId2DB(MaTaiKaShop taiKaShop,  Long ts) throws IOException, CSaveTaiKa2DBException {
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



    @Async
    @Override
    public void asyncGenerateAndSaveDb(String userName, Long activeId, List<MaTaiKaShop> taiKaList) throws Exception {
        long start = System.currentTimeMillis();

        log.info("活动: {}, 生成商铺台卡店铺微信公众号带参二维码， 店铺数量为: {}", activeId, taiKaList.size());
        this.generateTaiKaWeChatOfficialQrCode(taiKaList);

        log.info("活动: {}, 保存商铺台卡店铺信息", activeId);
        this.saveBatch(taiKaList);
        sysBaseAPI.sendSysAnnouncement(new MessageDTO("admin", userName, "商铺台卡店铺物料生成二维码操作完成"
                , String.format("活动：%s, 商铺台卡店铺物料生成二维码操作完成， 导入店铺量: %d， 耗时： %d毫秒", activeId, taiKaList.size(), System.currentTimeMillis() - start)));
    }

    @Async
    @Override
    public void asyncDownloadActiveTaiKaQrCodeAndZip(String userName, Long activeId, String srcSource) throws IOException {
        log.info("开始查询活动: {}对应台卡带参微信二维码信息", activeId);
        List<MaActiveTaiKaQrCodeUrl> activeTaiKaQrCodeUrlList = taikaShopMapper.selectTaiKaQrCodeByActiveId(activeId);
        // 下载活动台卡店铺微信二维码

        String finalActiveDirPath = Paths.get(srcSource, String.valueOf(activeId)).toString();
        FileUtil.del(finalActiveDirPath);
        FileUtil.mkdir(finalActiveDirPath);
        log.info("活动: {}, 开始下载台卡店铺带参微信二维码图片， 总计: {}张图片", activeId, activeTaiKaQrCodeUrlList.size());
        int qrCodeSize = activeTaiKaQrCodeUrlList.size();
        int pos = 0;
        for (MaActiveTaiKaQrCodeUrl taiKaQrCodeUrl : activeTaiKaQrCodeUrlList) {
            pos++;
            String marketName = taiKaQrCodeUrl.getMarketName();
            String shopId = taiKaQrCodeUrl.getShopId();
            String shopName = taiKaQrCodeUrl.getShopName();
            String qrCodeUrl = taiKaQrCodeUrl.getTaiKaShopQrCodeUrl();
            log.info("活动: {}, 开始下载台卡店铺带参微信二维码图片， 当前第: {}张二维码, 总计: {}张二维码,", activeId, pos, qrCodeSize);
            // 图片存储路径为： 目的目录+市场
            Path taiKaSaveDirPath = Paths.get(finalActiveDirPath, marketName);
            String taiKaSaveDir = taiKaSaveDirPath.toString();
            if (!FileUtil.exist(taiKaSaveDirPath.toFile())) {
                FileUtil.mkdir(taiKaSaveDirPath.toFile());
            }
            FileUtils.copyURLToFile(new URL(qrCodeUrl), Paths.get(taiKaSaveDir, shopId + "_" + shopName + ".png").toFile());
        }

        sysBaseAPI.sendSysAnnouncement(new MessageDTO("admin", userName,
                "下载微信二维码图片到服务器完成",
                String.format("下载活动编号： %s商位台卡店铺物料带参二维码图片到服务器完成，累计下载: %d张带参二维码!!!, 开始压缩二维码图...", activeId, qrCodeSize)));

        String targetActiveSrcSource = Paths.get(srcSource, String.valueOf(activeId)).toString();
        String targetFile = targetActiveSrcSource + ".zip";
        log.info("活动: {} 开始压缩微信公众号带参二维码目录: {} 到文件: {}中", activeId, targetActiveSrcSource, targetFile);
        // 删除压缩好的包
        File file = new File(targetFile);
        FileUtil.del(file);
        // 先将文件压缩成active_id.zip，再删除此文件
        ZipUtil.zip(targetActiveSrcSource, targetFile);
        sysBaseAPI.sendSysAnnouncement(new MessageDTO("admin", userName,
                "下载微信二维码图片到服务器并且压缩完成",
                String.format("下载活动编号： %s商位台卡店铺物料带参二维码图片到服务器并且压缩完成， 总计图片张数: %d, 可以下载微信带参二维码!", activeId, qrCodeSize)));
    }

}
