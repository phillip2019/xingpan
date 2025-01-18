package org.jeecg.modules.ibf.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.ibf.entity.IbfMarketResource;
import org.jeecg.modules.ibf.entity.IbfReportingSummary;
import org.jeecg.modules.ibf.mapper.IbfReportingSummaryMapper;
import org.jeecg.modules.ibf.service.IIbfMarketResourceService;
import org.jeecg.modules.ibf.service.IIbfReportingSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 填报发布汇总
 * @Author: jeecg-boot
 * @Date:   2025-01-17
 * @Version: V1.0
 */
@DS("ibf")
@Service
public class IbfReportingSummaryServiceImpl extends ServiceImpl<IbfReportingSummaryMapper, IbfReportingSummary> implements IIbfReportingSummaryService {


    @Autowired
    private IIbfMarketResourceService ibfMarketResourceService;

    @Override
    public void copy(IbfReportingSummary record) {
        // 复制发布记录，复制发布数据
        String curMonth = record.getMonthCol();
        // 复制资源-资源
        List<IbfMarketResource> list = ibfMarketResourceService.list(new LambdaQueryWrapper<IbfMarketResource>().eq(IbfMarketResource::getMonthCol, curMonth)
                .eq(IbfMarketResource::getIsDeleted, "0")
                .eq(IbfMarketResource::getIsPublish, 1)
                .eq()
        );

    }
}
