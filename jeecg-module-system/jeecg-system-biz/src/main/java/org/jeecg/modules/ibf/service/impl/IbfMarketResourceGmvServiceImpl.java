package org.jeecg.modules.ibf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.ibf.entity.IbfMarketResourceFlow;
import org.jeecg.modules.ibf.entity.IbfMarketResourceGmv;
import org.jeecg.modules.ibf.mapper.IbfMarketResourceGmvMapper;
import org.jeecg.modules.ibf.service.IIbfMarketResourceGmvService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 月市场资源-市场成交额填报
 * @Author: jeecg-boot
 * @Date:   2025-01-15
 * @Version: V1.0
 */
@Service
public class IbfMarketResourceGmvServiceImpl extends ServiceImpl<IbfMarketResourceGmvMapper, IbfMarketResourceGmv> implements IIbfMarketResourceGmvService {
    @Override
    public IbfMarketResourceGmv checkUnique(String shortMarketId, String monthCol) {
        QueryWrapper<IbfMarketResourceGmv> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("short_market_id", shortMarketId)
                .eq("month_col", monthCol);
        return this.getOne(queryWrapper);
    }
}
