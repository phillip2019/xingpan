package org.jeecg.modules.demo.et.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.demo.et.entity.EtChinagoods;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * @Description: 埋点服务接口
 * @Author: xiaowei.song
 * @Date:   2022-11-22
 * @Version: V1.0
 */
public interface IEtChinagoodsService extends IService<EtChinagoods> {
    /**
     * 查询kafka消息
     * @param etChinagoods 查询埋点实例参数
     * @param pageNo 页吗
     * @param pageSize 页面尺寸
     * @param req 请求实例
     * @return 页面
     * @throws ParseException
     */
    IPage<EtChinagoods> queryKafkaMessage(EtChinagoods etChinagoods, Integer pageNo, Integer pageSize, HttpServletRequest req) throws ParseException;

}
