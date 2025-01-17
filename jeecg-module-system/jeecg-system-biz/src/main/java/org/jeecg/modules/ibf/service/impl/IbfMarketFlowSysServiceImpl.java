package org.jeecg.modules.ibf.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.ibf.entity.IbfMarketFlowSys;
import org.jeecg.modules.ibf.mapper.IbfMarketFlowSysMapper;
import org.jeecg.modules.ibf.service.IIbfMarketFlowSysService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: ibf_market_flow_sys
 * @Author: jeecg-boot
 * @Date:   2025-01-17
 * @Version: V1.0
 */
@DS("ibf")
@Service
public class IbfMarketFlowSysServiceImpl extends ServiceImpl<IbfMarketFlowSysMapper, IbfMarketFlowSys> implements IIbfMarketFlowSysService {

}
