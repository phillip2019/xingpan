package org.jeecg.modules.ma.service;

import org.jeecg.modules.cg.entity.CgShopsZf;
import org.jeecg.modules.ma.entity.MaTaiKaShop;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
 * @Description: ma_tai_ka_shop
 * @Author: jeecg-boot
 * @Date:   2023-04-03
 * @Version: V1.0
 */
public interface IMaTaiKaShopService extends IService<MaTaiKaShop> {

    /**
     * 下载活动台卡店铺二维码图片到指定目录
     * @param userName 异步消息发送目标人
     * @param activeId 活动编号
     * @param taiKaList 台卡店铺编号列表
     * @throws Exception 异常
     */
    void asyncGenerateAndSaveDb(String userName, Long activeId, List<MaTaiKaShop> taiKaList) throws Exception;

    /**
     * 下载活动台卡店铺二维码图片到指定目录并压缩成zip文件
     * @param userName 异步消息发送目标人
     * @param activeId 活动编号
     * @param srcSource 存放目录
     * @throws IOException 网络异常
     */
    void asyncDownloadActiveTaiKaQrCodeAndZip(String userName, Long activeId, String srcSource) throws IOException;
}