package org.jeecg.modules.cg.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.cg.entity.CgDeptIndexValue;
import org.jeecg.modules.cg.entity.CgShopsZf;
import org.jeecg.modules.cg.mapper.CgDeptIndexValueMapper;
import org.jeecg.modules.cg.mapper.CgShopsZfMapper;
import org.jeecg.modules.cg.service.ICgDeptIndexValueService;
import org.jeecg.modules.cg.service.ICgShopsZfService;
import org.jeecg.modules.system.entity.SysCategory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 店铺维度拉链表
 * @Author: jeecg-boot
 * @Version: V1.0
 */
@DS("presto")
@Service
public class CgCgShopsZfServiceImpl extends ServiceImpl<CgShopsZfMapper, CgShopsZf> implements ICgShopsZfService {

    @Override
    public List<CgShopsZf> queryListByIds(List<String> shopIdsFE) {
        List<CgShopsZf> cgShopsZfList = baseMapper.selectList(new LambdaQueryWrapper<CgShopsZf>()
                .eq(CgShopsZf::getLang, "zh")
                .eq(CgShopsZf::getIsShopOn, "Y")
                .eq(CgShopsZf::getEndDate, "9999-12-31")
                .in(shopIdsFE.size() > 0, CgShopsZf::getShopId, shopIdsFE));
        return cgShopsZfList;
    }
}
