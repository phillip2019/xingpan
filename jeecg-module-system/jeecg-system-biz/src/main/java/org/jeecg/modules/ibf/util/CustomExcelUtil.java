package org.jeecg.modules.ibf.util;

/**
 * @author xiaowei.song
 * @version v1.0.0
 * @description TODO
 * @date 15/1/2025 下午 3:38
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ibf.entity.IbfCommonEntity;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


public class  CustomExcelUtil <T extends IbfCommonEntity, S extends IService<T>> {

    /**issues/2933 JeecgController注入service时改用protected修饰，能避免重复引用service*/
    @Autowired
    protected S service;

    @Lazy
    @Autowired
    private CommonAPI commonApi;


    @Value("${jeecg.path.upload}")
    private String upLoadPath;
    /**
     * 根据每页sheet数量导出多sheet
     *
     * @param request
     * @param object 实体类
     * @param clazz 实体类class
     * @param title 标题
     * @param exportFields 导出字段自定义
     * @param pageNum 每个sheet的数据条数
     * @param request
     */
    public ModelAndView exportXlsSheet(HttpServletRequest request, T object, Class<T> clazz, String title, String exportFields, Integer pageNum) {
        // Step.1 组装查询条件
        QueryWrapper<T> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // Step.2 计算分页sheet数据
        double total = service.count();
        int count = (int)Math.ceil(total/pageNum);
        //update-begin-author:liusq---date:20220629--for: 多sheet导出根据选择导出写法调整 ---
        // Step.3  过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        //update-end-author:liusq---date:20220629--for: 多sheet导出根据选择导出写法调整 ---
        // Step.4 多sheet处理
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <=count ; i++) {
            Page<T> page = new Page<T>(i, pageNum);
            IPage<T> pageList = service.page(page, queryWrapper);
            List<T> exportList = pageList.getRecords();
            Map<String, Object> map = new HashMap<>(5);
            ExportParams exportParams=new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title+i,upLoadPath);
            exportParams.setType(ExcelType.XSSF);
            //map.put("title",exportParams);
            //表格Title
            map.put(NormalExcelConstants.PARAMS,exportParams);
            //表格对应实体
            map.put(NormalExcelConstants.CLASS,clazz);
            //数据集合
            map.put(NormalExcelConstants.DATA_LIST, exportList);
            listMap.add(map);
        }
        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, title);
        mv.addObject(NormalExcelConstants.MAP_LIST, listMap);
        return mv;
    }
}
