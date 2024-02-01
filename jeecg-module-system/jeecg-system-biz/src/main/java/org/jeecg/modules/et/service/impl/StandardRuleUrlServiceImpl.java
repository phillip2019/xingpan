package org.jeecg.modules.et.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.et.entity.StandardRuleUrl;
import org.jeecg.modules.et.mapper.StandardRuleUrlMapper;
import org.jeecg.modules.et.service.StandardRuleUrlService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 平台标准化页面名称规则
 * @Author: jeecg-boot
 * @Date:   2024-01-31
 * @Version: V1.0
 */
@DS("source")
@Service
@Slf4j
public class StandardRuleUrlServiceImpl extends ServiceImpl<StandardRuleUrlMapper, StandardRuleUrl> implements StandardRuleUrlService {

}
