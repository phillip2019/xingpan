package org.jeecg.modules.ibf.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.ibf.entity.IbfReportingSummary;
import org.jeecg.modules.ibf.mapper.IbfReportingSummaryMapper;
import org.jeecg.modules.ibf.service.IIbfReportingSummaryService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 填报发布汇总
 * @Author: jeecg-boot
 * @Date:   2025-01-17
 * @Version: V1.0
 */
@DS("ibf")
@Service
public class IbfReportingSummaryServiceImpl extends ServiceImpl<IbfReportingSummaryMapper, IbfReportingSummary> implements IIbfReportingSummaryService {

}
