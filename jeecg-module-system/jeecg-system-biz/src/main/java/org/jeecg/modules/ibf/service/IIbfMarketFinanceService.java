package org.jeecg.modules.ibf.service;

import org.jeecg.modules.ibf.entity.IbfMarketFinance;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 业财一体-财务填报
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
public interface IIbfMarketFinanceService extends IService<IbfMarketFinance> {

    /**
     * 校验唯一性
     * @param shortMarketId 短市场ID
     * @param monthCol 月份
     * @return IbfMarketFinance
     */
    IbfMarketFinance checkUnique(String shortMarketId, String monthCol);
}
