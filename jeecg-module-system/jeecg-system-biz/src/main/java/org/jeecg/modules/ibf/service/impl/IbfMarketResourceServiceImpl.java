package org.jeecg.modules.ibf.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.ibf.entity.IbfMarketResource;
import org.jeecg.modules.ibf.mapper.IbfMarketResourceMapper;
import org.jeecg.modules.ibf.service.IIbfMarketResourceService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 业财一体-市场资源填报表
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
@DS("ibf")
@Service
public class IbfMarketResourceServiceImpl extends ServiceImpl<IbfMarketResourceMapper, IbfMarketResource> implements IIbfMarketResourceService {

}
