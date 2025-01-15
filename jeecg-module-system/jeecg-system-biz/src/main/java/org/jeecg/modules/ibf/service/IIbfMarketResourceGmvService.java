package org.jeecg.modules.ibf.service;

import org.jeecg.modules.ibf.entity.IbfMarketResourceFlow;
import org.jeecg.modules.ibf.entity.IbfMarketResourceGmv;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 月市场资源-市场成交额填报
 * @Author: jeecg-boot
 * @Date:   2025-01-15
 * @Version: V1.0
 */
public interface IIbfMarketResourceGmvService extends IService<IbfMarketResourceGmv> {
    IbfMarketResourceGmv checkUnique(String shortMarketId, String monthCol);
}
