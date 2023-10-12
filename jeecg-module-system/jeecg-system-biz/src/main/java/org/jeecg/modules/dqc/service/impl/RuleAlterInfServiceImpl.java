package org.jeecg.modules.dqc.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.dqc.entity.RuleAlterInf;
import org.jeecg.modules.dqc.mapper.RuleAlterInfMapper;
import org.jeecg.modules.dqc.service.IRuleAlterInfService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 数据质量预警规则
 * @Author: jeecg-boot
 * @Date:   2023-10-12
 * @Version: V1.0
 */
@DS("griffin")
@Service
@Slf4j
public class RuleAlterInfServiceImpl extends ServiceImpl<RuleAlterInfMapper, RuleAlterInf> implements IRuleAlterInfService {

}
