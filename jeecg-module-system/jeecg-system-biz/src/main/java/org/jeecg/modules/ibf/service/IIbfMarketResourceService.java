package org.jeecg.modules.ibf.service;

import org.jeecg.modules.ibf.entity.IbfMarketResource;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 业财一体-市场资源填报表
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
public interface IIbfMarketResourceService extends IService<IbfMarketResource> {

    IbfMarketResource checkUnique(String shortMarketId, String monthCol);
}
