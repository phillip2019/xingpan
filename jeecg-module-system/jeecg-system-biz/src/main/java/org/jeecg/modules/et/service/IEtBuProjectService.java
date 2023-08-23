package org.jeecg.modules.et.service;

import org.jeecg.modules.et.entity.EtBuProject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.et.entity.EtEventMaterial2;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: et_bu_project
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
public interface IEtBuProjectService extends IService<EtBuProject> {

    ModelAndView exportXls(HttpServletRequest request, EtBuProject etBuProject, Class<EtEventMaterial2> clazz, String title);
}
