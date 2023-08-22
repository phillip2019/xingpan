package org.jeecg.modules.et.service;

import org.jeecg.modules.et.entity.EtClientEvent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: et_client_event
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
public interface IEtClientEventService extends IService<EtClientEvent> {

    boolean saveClientEvent(String clientId, String eventIds, String lastEventIds);
}
