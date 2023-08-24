package org.jeecg.modules.et.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.et.entity.EtEventLocal;
import org.jeecg.modules.et.mapper.EtEventLocalMapper;
import org.jeecg.modules.et.service.IEtEventLocalService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: CK中实时埋点事件
 * @Author: jeecg-boot
 * @Date:   2023-08-24
 * @Version: V1.0
 */
@Slf4j
@DS("ck")
@Service
public class EtEventLocalServiceImpl extends ServiceImpl<EtEventLocalMapper, EtEventLocal> implements IEtEventLocalService {

}
