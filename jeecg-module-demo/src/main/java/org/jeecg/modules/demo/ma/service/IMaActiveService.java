package org.jeecg.modules.demo.ma.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.demo.ma.entity.MaActive;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.demo.ma.entity.MaActiveYlbMaterial;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @Description: 活动
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
public interface IMaActiveService extends IService<MaActive> {


    /**
     * 导入易拉宝物料数据
     *
     * @param activeId 活动编号
     * @param request 请求体
     * @param response 返回体
     * @param clazz
     * @return 导入消息
     */
    Result<?> importYlbExcel(Long activeId, HttpServletRequest request, HttpServletResponse response, Class<MaActiveYlbMaterial> clazz);

    /**
     * 下载活动二维码图片到指定目录
     * @param activeId 活动编号
     * @param srcSource 存放目录
     * @throws IOException 网络异常
     */
    void downloadActiveQrCode(Long activeId, String srcSource) throws IOException;
}
