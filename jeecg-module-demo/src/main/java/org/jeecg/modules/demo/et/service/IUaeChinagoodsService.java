package org.jeecg.modules.demo.et.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.demo.et.entity.UaeChinagoods;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * @Description: 埋点服务接口
 * @Author: xiaowei.song
 * @Date:   2022-11-22
 * @Version: V1.0
 */
public interface IUaeChinagoodsService extends IService<UaeChinagoods> {
    /**
     * 查询kafka消息
     * @param uaeChinagoods 查询uae实例
     * @param pageNo 页吗
     * @param pageSize 页面尺寸
     * @param req 请求实例
     * @return 页面
     * @throws ParseException
     */
    IPage<UaeChinagoods> queryKafkaMessage(UaeChinagoods uaeChinagoods, Integer pageNo, Integer pageSize, HttpServletRequest req) throws ParseException;
}
