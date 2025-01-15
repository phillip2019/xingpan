package org.jeecg.modules.ibf.service;

import org.jeecg.modules.ibf.entity.IbfMarketResourceFlow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 月市场资源-市场流量
 * @Author: jeecg-boot
 * @Date:   2025-01-15
 * @Version: V1.0
 */
public interface IIbfMarketResourceFlowService extends IService<IbfMarketResourceFlow> {

    IbfMarketResourceFlow checkUnique(String shortMarketId, String monthCol);
}
