package org.jeecg.modules.ibf.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ibf.entity.IbfCommonEntity;
import org.jeecg.modules.ibf.entity.IbfMarketFinance;
import org.jeecg.modules.ibf.entity.IbfMarketResource;
import org.jeecg.modules.ibf.util.IbfDateUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: Controller基类
 * @Author: 458712sxw@163.com
 * @Date: 2025-01-15 15:32:09
 * @Version: 1.0
 */
@Slf4j
public class CustomController<T extends IbfCommonEntity, S extends IService<T>> extends JeecgController<T, S> {
    @Lazy
    @Autowired
    private CommonAPI commonApi;

    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    protected static final String DICT_CODE = "short_market_id";


    /**
     * 导出excel
     *
     * @param request
     */
    @Override
    protected ModelAndView exportXls(HttpServletRequest request, T object, Class<T> clazz, String title) {
        // Step.1 组装查询条件
        QueryWrapper<T> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id",selectionList);
        }
        // 过滤当前租户数据
        List<String> shortMarketIdList = Arrays.asList(org.apache.commons.lang.StringUtils.split(sysUser.getRelTenantIds(), ','));
        if (!shortMarketIdList.isEmpty()) {
            queryWrapper.in("short_market_id", shortMarketIdList);
        }

        // Step.2 获取导出数据
        List<T> exportList = service.list(queryWrapper);
        exportList.forEach(T::customDB2VO);

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, title);
        mv.addObject(NormalExcelConstants.CLASS, clazz);
        //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
        ExportParams  exportParams=new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title);
        exportParams.setImageBasePath(upLoadPath);
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.PARAMS,exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<T> clazz) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Date now = new Date();
        List<DictModel> dictModelList = commonApi.queryEnableDictItemsByCode(DICT_CODE);
        // 将dictModelList的value转换成数组
        List<String> shortMarketIdList = dictModelList.stream().map(DictModel::getValue).collect(Collectors.toList());

        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<T> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                Set<String> errShortMarketIdSet = new HashSet<>();

                String curMonth = IbfDateUtil.getCurrentMonth();
                // 校验市场，市场必须为 shortMarketId
                for (T et : list) {
                    String shortMarketId = et.getShortMarketId();
                    // 只有列表中的shortMarketId在字典中才行
                    if (!shortMarketIdList.contains(shortMarketId)) {
                        return Result.error("市场编号:【" + shortMarketId + "】不存在");
                    }

                    // 过滤当前租户数据
                    List<String> permissShortMarketIdList = Arrays.asList(org.apache.commons.lang.StringUtils.split(sysUser.getRelTenantIds(), ','));
                    if (!permissShortMarketIdList.contains(shortMarketId)) {
                        errShortMarketIdSet.add(et.getShortMarketId());
                        continue;
                    }
                    // TODO 先将校验月份逻辑注释
//                    // 校验月份
//                    String monthCol = et.getMonthCol();
//                    // 校验月份格式
//                    if (StringUtils.isBlank(monthCol) || !monthCol.matches("\\d{4}-\\d{2}")) {
//                        return Result.error("月份格式错误:【" + monthCol + "】");
//                    }
//                    // 校验月份是否大于当前月份
//                    try {
//                        // monthCol 格式为 yyyy-MM
//                        // 只能导入当前月份（含）之前月份的数据，不能超过2个月
//                        if (IbfDateUtil.calculateMonthDifference(curMonth, monthCol) > 1 ) {
//                            return Result.error( String.format("只能导入近2个月的数据，当前所属月份为: 【 %s 】， 填报月份:【 %s 】", curMonth, monthCol));
//                        }
//
//                        // 不能导入之后月份的数据
//                        if (IbfDateUtil.calculateMonthDifference(curMonth, monthCol) < 0 ) {
//                            return Result.error(String.format("填报月份不能大于当前月份:【 %s 】，不支持提前导入填报: 【%s】月份的数据", curMonth, monthCol));
//                        }
//                    } catch (Exception e) {
//                        log.error("月份格式错误:【" + monthCol + "】", e);
//                        return Result.error("月份格式错误:【" + monthCol + "】");
//                    }
                }

                // 如果有错误的市场编号，返回错误消息
                if (!errShortMarketIdSet.isEmpty()) {
                    return Result.error("无操作权限的市场编号:【" + org.apache.commons.lang.StringUtils.join(errShortMarketIdSet, ",") + "】，请检查权限!");
                }

                // 三元组唯一性校验 short_market_id，monthCol, isPublish
                for (T et : list) {
                    String shortMarketId = et.getShortMarketId();
                    String monthCol = et.getMonthCol();
                    Integer isPublish = et.getIsPublish();
                    // 校验唯一性
                    List<T> tList = service.list(new QueryWrapper<T>()
                            .eq("short_market_id", shortMarketId)
                            .eq("month_col", monthCol)
                            // 导入的都是未发布的数据
                            .eq("is_publish", isPublish)
                            .last("limit 1")
                    );
                    // TODO 先注释此逻辑，后续再打开
//                    et.setIsPublish(0);
                    // 插入月份，计算当前月份和填写月份的差值
                    // 当前日期月份

                    // 默认选择第一个
                    if (!tList.isEmpty()) {
                        String id = tList.get(0).getId();
                        log.info("已存在{}市场{}月的填报数据未发布数据, id为：{}", shortMarketId, monthCol, id);
                        et.setId(tList.get(0).getId());
                    }
                }
                list.forEach(T::customVO2DB);
                //update-begin-author:taoyan date:20190528 for:批量插入数据
                long start = System.currentTimeMillis();
                service.saveOrUpdateBatch(list);
                //400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
                //1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                //update-end-author:taoyan date:20190528 for:批量插入数据
                return Result.ok("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                //update-begin-author:taoyan date:20211124 for: 导入数据重复增加提示
                String msg = e.getMessage();
                log.error(msg, e);
                if (msg != null && msg.contains("Duplicate entry")) {
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
}
