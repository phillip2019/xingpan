package org.jeecg.modules.et.service;

import org.jeecg.modules.et.entity.EtBuProjectEvent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: et_bu_project_event
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
public interface IEtBuProjectEventService extends IService<EtBuProjectEvent> {

    boolean saveBuProjectEvent(String buProjectId, String eventIds, String lastEventIds);
}
