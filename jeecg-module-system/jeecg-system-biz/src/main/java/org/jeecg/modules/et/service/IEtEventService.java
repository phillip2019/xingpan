package org.jeecg.modules.et.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.et.entity.EtEvent;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.et.entity.EtEventMaterial;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: et_event
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
public interface IEtEventService extends IService<EtEvent> {

    Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<EtEventMaterial> etEventClass);
}
