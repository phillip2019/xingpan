package org.jeecg.modules.demo.et.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.EvictingQueue;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.demo.et.entity.EventTracking;
import org.jeecg.modules.demo.et.entity.UaeChinagoods;
import org.jeecg.modules.demo.et.mapper.UaeChinagoodsMapper;
import org.jeecg.modules.demo.et.service.IUaeChinagoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

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

    private Queue<UaeChinagoods> eventTrackingQueue = EvictingQueue.create(50000);

    @PostConstruct
    public void init() {
        etStream.map((anonymousId, et) -> {
            eventTrackingQueue.add(et.toChinagoods());
            return new KeyValue<>(et.getAnonymousId(), et.toChinagoods());
        });
    }

    @Override
    public IPage<UaeChinagoods> queryKafkaMessage(UaeChinagoods uaeChinagoods, Integer pageNo, Integer pageSize, HttpServletRequest req) throws ParseException {
        String env = req.getParameter("env");
        String distinctId = uaeChinagoods.getDistinctId();
        String event = uaeChinagoods.getEvent();
        String project = req.getParameter("project");
        String platformType = req.getParameter("platformType");
        long beginCreatedAt = 0L, endCreatedAt = 0L;
        // 基于时间查询
        String[] createdAtArr = req.getParameterValues("createdAtArr[]");
        if (createdAtArr != null && createdAtArr.length == 2) {
            beginCreatedAt = DateUtils.parseTimestamp(createdAtArr[0], "yyyy-MM-dd HH:mm:ss").toInstant().getEpochSecond() * 1000;
            endCreatedAt = DateUtils.parseTimestamp(createdAtArr[1], "yyyy-MM-dd HH:mm:ss").toInstant().getEpochSecond() * 1000;
        }
        long finalBeginCreatedAt = beginCreatedAt;
        long finalEndCreatedAt = endCreatedAt;

        List<UaeChinagoods> resultList = eventTrackingQueue.stream().filter(et -> {
            boolean ret = true;
            if (StringUtils.isNotBlank(distinctId)) {
                ret = StringUtils.equals(distinctId, et.getDistinctId());
            }

            if (StringUtils.isNotBlank(event)) {
                ret = StringUtils.equals(event, et.getEvent());
            }

            if (StringUtils.isNotBlank(project)) {
                ret = StringUtils.equals(project, et.getProject());
            }

            // platformType
            if (StringUtils.isNotBlank(platformType)) {
                ret = StringUtils.equals(platformType, et.getPlatformType());
            }

            // createdAt
            if (finalBeginCreatedAt != 0 && finalEndCreatedAt != 0) {
                ret = false;
                long createdAt = Long.parseLong(et.getCreatedAt());
                if (createdAt >= finalBeginCreatedAt && createdAt <= finalEndCreatedAt) {
                    ret = true;
                }
            }
            return ret;
        }).collect(Collectors.toList())
        ;

        Page<UaeChinagoods> page = new Page<UaeChinagoods>(pageNo, pageSize);
        page.setTotal(resultList.size())
             .setRecords(resultList)
             .setPages(1)
             .setCurrent(1)
             .setSize(resultList.size())
        ;
        return page;
    }
}
