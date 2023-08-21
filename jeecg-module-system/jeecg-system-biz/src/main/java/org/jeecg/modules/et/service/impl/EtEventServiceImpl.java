package org.jeecg.modules.et.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.et.entity.*;
import org.jeecg.modules.et.mapper.EtEventMapper;
import org.jeecg.modules.et.mapper.EtEventPropertyMapper;
import org.jeecg.modules.et.service.IEtEventPropertyService;
import org.jeecg.modules.et.service.IEtEventService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @Description: 埋点事件
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Slf4j
@Service
public class EtEventServiceImpl extends ServiceImpl<EtEventMapper, EtEvent> implements IEtEventService {

    @Autowired
    private EtEventMapper eventMapper;

    @Autowired
    private EtEventPropertyMapper eventPropertyMapper;

    @Autowired
    private IEtEventPropertyService eventPropertyService;

    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<EtEventMaterial> clazz) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<EtEventMaterial> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                log.info("导入埋点元事件条数: {}", list.size());
                long start = System.currentTimeMillis();
                batchSaveEvent(list);
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                //update-end-author:taoyan date:20190528 for:批量插入数据
                return Result.ok("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                //update-begin-author:taoyan date:20211124 for: 导入数据重复增加提示
                String msg = e.getMessage();
                log.error(msg, e);
                if(msg != null && msg.contains("Duplicate entry")){
                    return Result.error("文件导入失败:有重复数据！");
                } else {
                    return Result.error("文件导入失败:" + e.getMessage());
                }
                //update-end-author:taoyan date:20211124 for: 导入数据重复增加提示
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    private void batchSaveEvent(List<EtEventMaterial> list) {
        log.info("开始保存埋点事件，事件条数为: {}...", list.size());
        EtEvent event = null;
        String eventName = "";
        String eventZhName = "";
        String propertyName = "";
        String propertyZhName = "";
        String scene = "";
        List<EtEventProperty> eventPropertyList = new ArrayList<>();
        for (EtEventMaterial eventMaterial : list) {
            if (StringUtils.isNotBlank(eventMaterial.getScene())) {
                scene = eventMaterial.getScene();
            }
            if (StringUtils.isNotBlank(eventMaterial.getName())) {
                event = new EtEvent();
                eventName = eventMaterial.getName();
                eventZhName = eventMaterial.getZhName();
                log.info("开始保存事件: {}，事件中文名: {}...", eventName, eventZhName);
                event.setId(IdUtil.randomUUID())
                        .setName(eventName)
                        .setZhName(eventZhName)
                        .setOperDesc(eventMaterial.getOperDesc())
                        .setType(eventMaterial.getType())
                        .setTriggerTiming(eventMaterial.getTriggerTiming())
                        .setEventDesc(eventMaterial.getEventDesc())
                        .setIsPresetEvent(eventMaterial.getIsPresetEvent())
                        .setScene(scene)
                        .setStatus(eventMaterial.getStatus())
                        ;
                event.setCreateBy(((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername());
                event.setCreateTime(new Date());
                // TODO 进行事件查询，前置校验
                // 提交事件
                eventMapper.insertEventReturnId(event);
                log.info("保存事件: {}，事件中文名: {}成功，事件编号: {}", eventName, eventZhName, event.getId());
            }
            assert event != null;
            if (StringUtils.isBlank(eventMaterial.getPropertyName())) {
                log.error("事件ID: {}, 事件: {}, 事件中文名: {}, 属性名称为空: 中文名为: {}", event.getId(), eventName, eventZhName, eventMaterial.getPropertyZhName());
                continue;
            }
            propertyName = eventMaterial.getPropertyName();
            propertyZhName = eventMaterial.getPropertyZhName();
            log.info("开始保存事件ID: {}, 事件: {}，事件中文名: {}，属性:{}，属性中文名: {}", event.getId(), eventName, eventZhName, propertyName, propertyZhName);
            if (StringUtils.isBlank(propertyZhName)) {
                log.error("事件ID: {}, 事件: {}, 事件中文名: {}, 属性名称: {}, 中文名为: {}", event.getId(), eventName, eventZhName, propertyName, propertyZhName);
            }
            EtEventProperty eventProperty = new EtEventProperty();
            eventProperty
                    .setId(IdUtil.randomUUID())
                    .setEventId(event.getId())
                    .setName(propertyName)
                    .setZhName(propertyZhName)
                    .setType(eventMaterial.getPropertyType())
                    .setExample(eventMaterial.getPropertyExample())
                    .setPropertyDesc(eventMaterial.getPropertyDesc())
                    .setCreateBy(((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername())
                    .setCreateTime(new Date());
//            eventPropertyMapper.insert(eventProperty);
            eventPropertyList.add(eventProperty);
        }
        log.info("开始批量保存属性，数量: {}...", eventPropertyList.size());
        if (!CollectionUtils.isEmpty(eventPropertyList)) {
            eventPropertyService.saveBatch(eventPropertyList);
            log.info("批量保存事件属性，数量: {}完成", eventPropertyList.size());
        }
    }

    @Override
    public ModelAndView exportXls(HttpServletRequest request, EtEvent etEvent, Class<EtEventMaterial2> clazz, String title) {


        // step.1 组装查询条件查询数据
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<String> selectionList = new ArrayList<>();
        if (oConvertUtils.isNotEmpty(selections)) {
            selectionList = Arrays.asList(selections.split(","));
        }
        // Step.2 获取导出数据
        List<EtEventMaterial2> exportList = eventMapper.listEventMaterial2(etEvent, selectionList);

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
