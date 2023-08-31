package org.jeecg.modules.et.service;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.et.entity.EtEvent;
import org.jeecg.modules.et.entity.EtEventMaterial;
import org.jeecg.modules.et.entity.EtEventMaterial2;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Collection;

/**
 * @Description: et_event
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
public interface IEtEventService extends IService<EtEvent> {

    @Override
    boolean removeById(Serializable id);

    @Override
    boolean removeByIds(Collection<?> list);

    Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<EtEventMaterial> etEventClass);

    ModelAndView exportXls(HttpServletRequest request, EtEvent etEvent, Class<EtEventMaterial2> clazz, String title);

    boolean copy(EtEvent etEvent);
}
