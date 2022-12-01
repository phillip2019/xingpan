package org.jeecg.modules.demo.et.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.demo.et.entity.UaeChinagoods;
import org.jeecg.modules.demo.et.mapper.UaeChinagoodsMapper;
import org.jeecg.modules.demo.et.service.IUaeChinagoodsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: uae_chinagoods
 * @Author: jeecg-boot
 * @Date:   2022-11-22
 * @Version: V1.0
 */
@Service
@DS("ghost_sa")
public class UaeChinagoodsServiceImpl extends ServiceImpl<UaeChinagoodsMapper, UaeChinagoods> implements IUaeChinagoodsService {

}
