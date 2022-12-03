package org.jeecg.modules.demo.et.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.kafka.streams.kstream.KStream;
import org.jeecg.modules.demo.et.entity.EventTracking;
import org.jeecg.modules.demo.et.entity.UaeChinagoods;
import org.jeecg.modules.demo.et.mapper.UaeChinagoodsMapper;
import org.jeecg.modules.demo.et.service.IUaeChinagoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: uae_chinagoods
 * @Author: jeecg-boot
 * @Date:   2022-11-22
 * @Version: V1.0
 */
@Service
@DS("ghost_sa")
public class UaeChinagoodsServiceImpl extends ServiceImpl<UaeChinagoodsMapper, UaeChinagoods> implements IUaeChinagoodsService {
    @Autowired
    private KStream<String, EventTracking> etStream;

    /**
     * TODO 增加过滤，返回数据
     **/
    public void queryKafkaMessage(UaeChinagoods uaeChinagoods, HttpServletRequest req) {


        etStream.filter((anonymousId, et) -> {

        });
    }
}
