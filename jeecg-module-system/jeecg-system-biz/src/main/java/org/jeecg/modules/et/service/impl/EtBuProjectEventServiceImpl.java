package org.jeecg.modules.et.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.et.entity.EtBuProjectEvent;
import org.jeecg.modules.et.mapper.EtBuProjectEventMapper;
import org.jeecg.modules.et.service.IEtBuProjectEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.jeecg.common.util.CommonUtils.getDiff;

/**
 * @Description: et_bu_project_event
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Service
public class EtBuProjectEventServiceImpl extends ServiceImpl<EtBuProjectEventMapper, EtBuProjectEvent> implements IEtBuProjectEventService {


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    @Override
    public boolean saveBuProjectEvent(String buProjectId, String eventIds, String lastEventIds) {
        List<String> add = getDiff(lastEventIds, eventIds);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(add != null && add.size() > 0) {
            List<EtBuProjectEvent> list = new ArrayList<>(5);
            for (String p : add) {
                if(oConvertUtils.isNotEmpty(p)) {
                    EtBuProjectEvent buProjectEvent = new EtBuProjectEvent(buProjectId, p);
                    buProjectEvent.setId(IdUtil.randomUUID())
                            // 设置状态为初始化
                            .setStatus(1)
                            .setCreateTime(new Date())
                            .setCreateBy(loginUser.getUsername());
                    list.add(buProjectEvent);
                }
            }
            this.saveBatch(list);
        }

        List<String> deleteIds = getDiff(eventIds, lastEventIds);
        if(deleteIds != null && deleteIds.size() > 0) {
            for (String eventId : deleteIds) {
                this.remove(new QueryWrapper<EtBuProjectEvent>().lambda().eq(EtBuProjectEvent::getBuProjectId, buProjectId).eq(EtBuProjectEvent::getEventId, eventId));
            }
        }
        return true;
    }
}
