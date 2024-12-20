package org.jeecg.modules.ibf.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.ibf.entity.IbfMarketFlow;
import org.jeecg.modules.ibf.mapper.IbfMarketFlowMapper;
import org.jeecg.modules.ibf.service.IIbfMarketFlowService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 业财一体-每日填报市场流量
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
@DS("ibf")
@Service
public class IbfMarketFlowServiceImpl extends ServiceImpl<IbfMarketFlowMapper, IbfMarketFlow> implements IIbfMarketFlowService {

}
