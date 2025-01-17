package org.jeecg.modules.ibf.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.ibf.entity.IbfMarketResource;
import org.jeecg.modules.ibf.entity.IbfMarketResourceFlow;
import org.jeecg.modules.ibf.mapper.IbfMarketResourceFlowMapper;
import org.jeecg.modules.ibf.service.IIbfMarketResourceFlowService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 月市场资源-市场流量
 * @Author: jeecg-boot
 * @Date:   2025-01-15
 * @Version: V1.0
 */
@DS("ibf")
@Service
public class IbfMarketResourceFlowServiceImpl extends ServiceImpl<IbfMarketResourceFlowMapper, IbfMarketResourceFlow> implements IIbfMarketResourceFlowService {
    @Override
    public IbfMarketResourceFlow checkUnique(String shortMarketId, String monthCol) {
        QueryWrapper<IbfMarketResourceFlow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("short_market_id", shortMarketId)
                .eq("month_col", monthCol);
        return this.getOne(queryWrapper);
    }
}
