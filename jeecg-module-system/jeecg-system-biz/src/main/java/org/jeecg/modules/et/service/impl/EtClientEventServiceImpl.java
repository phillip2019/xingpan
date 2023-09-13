package org.jeecg.modules.et.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.et.entity.EtClientEvent;
import org.jeecg.modules.et.mapper.EtClientEventMapper;
import org.jeecg.modules.et.service.IEtClientEventService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static org.jeecg.common.util.CommonUtils.getDiff;

/**
 * @Description: et_client_event
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Service
public class EtClientEventServiceImpl extends ServiceImpl<EtClientEventMapper, EtClientEvent> implements IEtClientEventService {
    @Resource
    private EtClientEventMapper etClientEventMapper;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    @Override
    public boolean saveClientEvent(String clientId, String eventIds, String lastEventIds) {
        List<String> add = getDiff(lastEventIds, eventIds);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(add != null && add.size() > 0) {
            List<EtClientEvent> list = new ArrayList<>(5);
            for (String p : add) {
                if(oConvertUtils.isNotEmpty(p)) {
                    EtClientEvent clientEvent = new EtClientEvent(clientId, p);
                    clientEvent.setId(IdUtil.randomUUID())
                            .setStatus(2)
                            .setCreateTime(new Date())
                            .setCreateBy(loginUser.getUsername());
                    list.add(clientEvent);
                }
            }
            this.saveBatch(list);
        }

        List<String> deleteIds = getDiff(eventIds, lastEventIds);
        if(deleteIds != null && deleteIds.size() > 0) {
            for (String eventId : deleteIds) {
                this.remove(new QueryWrapper<EtClientEvent>().lambda().eq(EtClientEvent::getClientId, clientId).eq(EtClientEvent::getEventId, eventId));
            }
        }
        return true;
    }

    @Override
    public List<String> listEventIdByClientName(String clientName) {
        return etClientEventMapper.listEventIdByClientName(clientName);
    }
}
