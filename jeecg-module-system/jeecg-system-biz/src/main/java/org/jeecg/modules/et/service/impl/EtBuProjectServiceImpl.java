package org.jeecg.modules.et.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.et.entity.*;
import org.jeecg.modules.et.mapper.EtBuProjectEventMapper;
import org.jeecg.modules.et.mapper.EtBuProjectMapper;
import org.jeecg.modules.et.mapper.EtEventMapper;
import org.jeecg.modules.et.service.IEtBuProjectService;
import org.jeecg.modules.et.service.IEtEventPropertyService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: et_bu_project
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Service
public class EtBuProjectServiceImpl extends ServiceImpl<EtBuProjectMapper, EtBuProject> implements IEtBuProjectService {

    @Autowired
    private EtEventMapper eventMapper;

    @Autowired
    private EtBuProjectEventMapper buProjectEventMapper;

    @Autowired
    private IEtEventPropertyService eventPropertyService;

    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    @Override
    public ModelAndView exportXls(HttpServletRequest request, EtBuProject etBuProject, Class<EtEventMaterial2> clazz, String title) {
        // step.1 组装查询条件查询数据
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 过滤选中数据
        // 获取buProjectId
        String buProjectId = etBuProject.getId();

        EtBuProjectEvent buProjectEvent = new EtBuProjectEvent();
        buProjectEvent.setBuProjectId(buProjectId);
        QueryWrapper<EtBuProjectEvent> buProjectEventQueryWrapper = QueryGenerator.initQueryWrapper(buProjectEvent, null);
        List<EtBuProjectEvent> buProjectEventList = buProjectEventMapper.selectList(buProjectEventQueryWrapper);
        List<String> selectionList = buProjectEventList.stream().map(EtBuProjectEvent::getEventId).collect(Collectors.toList());

        // Step.2 获取导出数据
        List<EtEventMaterial2> exportList = eventMapper.listEventMaterial2(new EtEvent(), selectionList);

        // 拼装属性值
        EtEventProperty queryProperty = new EtEventProperty();
        for (EtEventMaterial2 et : exportList) {
            String id = et.getId();
            queryProperty.setEventId(id);
            QueryWrapper<EtEventProperty> queryWrapper = QueryGenerator.initQueryWrapper(queryProperty, null);
            List<EtEventProperty> etPropertyList = new ArrayList<>();
            EtEventProperty property = new EtEventProperty();
            property.setZhName("$预置属性");
            etPropertyList.add(property);
            List<EtEventProperty> propertyList = eventPropertyService.list(queryWrapper);
            etPropertyList.addAll(propertyList);
            et.setPropertyList(etPropertyList);
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, title);
        mv.addObject(NormalExcelConstants.CLASS, clazz);
        //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
        ExportParams exportParams=new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title);
        exportParams.setImageBasePath(upLoadPath);
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.PARAMS,exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }
}
