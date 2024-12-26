package org.jeecg.modules.ibf.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.ibf.entity.IbfMarketFinance;
import org.jeecg.modules.ibf.mapper.IbfMarketFinanceMapper;
import org.jeecg.modules.ibf.service.IIbfMarketFinanceService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 业财一体-财务填报
 * @Author: jeecg-boot
 * @Date: 2024-12-19
 * @Version: V1.0
 */
@DS("ibf")
@Service
public class IbfMarketFinanceServiceImpl extends ServiceImpl<IbfMarketFinanceMapper, IbfMarketFinance> implements IIbfMarketFinanceService {
    /**
     * 校验唯一性
     *
     * @param businessVersion 业务版本
     * @param shortMarketId   短市场ID
     * @param monthCol        月份
     * @return IbfMarketFinance
     */
    public IbfMarketFinance checkUnique(String businessVersion, String shortMarketId, String monthCol) {
        QueryWrapper<IbfMarketFinance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("business_version", businessVersion)
                .eq("short_market_id", shortMarketId)
                .eq("month_col", monthCol);
        return this.getOne(queryWrapper);
    }


}
