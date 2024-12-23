package org.jeecg.modules.ibf.service;

import org.jeecg.modules.ibf.entity.IbfMarketFlow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 业财一体-每日填报市场流量
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
public interface IIbfMarketFlowService extends IService<IbfMarketFlow> {

    IbfMarketFlow checkUnique(String businessVersion, String shortMarketId, String dateCol);
}
