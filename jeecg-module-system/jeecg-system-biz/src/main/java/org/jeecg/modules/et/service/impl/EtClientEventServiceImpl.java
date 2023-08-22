package org.jeecg.modules.et.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.IpUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.et.entity.EtClientEvent;
import org.jeecg.modules.et.mapper.EtClientEventMapper;
import org.jeecg.modules.et.service.IEtClientEventService;
import org.jeecg.modules.system.entity.SysRolePermission;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Description: et_client_event
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Service
public class EtClientEventServiceImpl extends ServiceImpl<EtClientEventMapper, EtClientEvent> implements IEtClientEventService {

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
        return false;
    }

    /**
     * 从diff中找出main中没有的元素
     * @param main
     * @param diff
     * @return
     */
    private List<String> getDiff(String main, String diff){
        if(oConvertUtils.isEmpty(diff)) {
            return null;
        }
        if(oConvertUtils.isEmpty(main)) {
            return Arrays.asList(diff.split(","));
        }

        String[] mainArr = main.split(",");
        String[] diffArr = diff.split(",");
        Map<String, Integer> map = new HashMap<>(5);
        for (String string : mainArr) {
            map.put(string, 1);
        }
        List<String> res = new ArrayList<String>();
        for (String key : diffArr) {
            if(oConvertUtils.isNotEmpty(key) && !map.containsKey(key)) {
                res.add(key);
            }
        }
        return res;
    }
}
