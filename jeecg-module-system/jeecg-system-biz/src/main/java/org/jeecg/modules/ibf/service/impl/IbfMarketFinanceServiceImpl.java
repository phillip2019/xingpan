package org.jeecg.modules.ibf.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.ibf.entity.IbfMarketFinance;
import org.jeecg.modules.ibf.mapper.IbfMarketFinanceMapper;
import org.jeecg.modules.ibf.service.IIbfMarketFinanceService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 业务一体-财务填报
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
@DS("ibf")
@Service
public class IbfMarketFinanceServiceImpl extends ServiceImpl<IbfMarketFinanceMapper, IbfMarketFinance> implements IIbfMarketFinanceService {

}
