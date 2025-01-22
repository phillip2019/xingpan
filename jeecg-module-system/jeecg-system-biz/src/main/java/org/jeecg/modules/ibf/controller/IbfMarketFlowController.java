package org.jeecg.modules.ibf.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ibf.entity.IbfMarketFinance;
import org.jeecg.modules.ibf.entity.IbfMarketFlow;
import org.jeecg.modules.ibf.service.IIbfMarketFlowService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 业财一体-每日填报市场流量
 * @Author: jeecg-boot
 * @Date: 2024-12-19
 * @Version: V1.0
 */
@Api(tags = "业财一体-每日填报市场流量")
@RestController
@RequestMapping("/ibf/ibfMarketFlow")
@Slf4j
public class IbfMarketFlowController extends JeecgController<IbfMarketFlow, IIbfMarketFlowService> {
    @Autowired
    private IIbfMarketFlowService ibfMarketFlowService;

    @Lazy
    @Autowired
    private CommonAPI commonApi;

    public static final String DICT_CODE = "short_market_id";

    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    /**
     * 分页列表查询
     *
     * @param ibfMarketFlow
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "业财一体-每日填报市场流量-分页列表查询")
    @ApiOperation(value = "业财一体-每日填报市场流量-分页列表查询", notes = "业财一体-每日填报市场流量-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<IbfMarketFlow>> queryPageList(IbfMarketFlow ibfMarketFlow,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        QueryWrapper<IbfMarketFlow> queryWrapper = QueryGenerator.initQueryWrapper(ibfMarketFlow, req.getParameterMap());
        // 直接获取当前用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isNotBlank(loginUser.getRelTenantIds())) {
            queryWrapper.in("short_market_id", Arrays.asList(StringUtils.split(loginUser.getRelTenantIds(), ',')));
        } else {
            queryWrapper.in("short_market_id", Collections.singletonList(""));
        }
        Page<IbfMarketFlow> page = new Page<IbfMarketFlow>(pageNo, pageSize);
        IPage<IbfMarketFlow> pageList = ibfMarketFlowService.page(page, queryWrapper);
        List<IbfMarketFlow> ibfMarketFlowList = pageList.getRecords();
        ibfMarketFlowList.forEach(IbfMarketFlow::customDB2VO);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param ibfMarketFlow
     * @return
     */
    @AutoLog(value = "业财一体-每日填报市场流量-添加")
    @ApiOperation(value = "业财一体-每日填报市场流量-添加", notes = "业财一体-每日填报市场流量-添加")
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody IbfMarketFlow ibfMarketFlow) {
        // 直接获取当前用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> shortMarketIdList = Arrays.asList(StringUtils.split(loginUser.getRelTenantIds(), ','));
        if (!shortMarketIdList.contains(ibfMarketFlow.getShortMarketId())) {
            return Result.ok(String.format("无法添加市场编号为: [%s]，请联系相关人员!", ibfMarketFlow.getShortMarketId()));
        }
        ibfMarketFlow.customVO2DB();
        ibfMarketFlowService.save(ibfMarketFlow);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param ibfMarketFlow
     * @return
     */
    @AutoLog(value = "业财一体-每日填报市场流量-编辑")
    @ApiOperation(value = "业财一体-每日填报市场流量-编辑", notes = "业财一体-每日填报市场流量-编辑")
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody IbfMarketFlow ibfMarketFlow) {
        // 直接获取当前用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> shortMarketIdList = Arrays.asList(StringUtils.split(loginUser.getRelTenantIds(), ','));
        if (StringUtils.isNotBlank(ibfMarketFlow.getShortMarketId()) && !shortMarketIdList.contains(ibfMarketFlow.getShortMarketId())) {
            return Result.ok(String.format("没有权限修改市场编号为: [%s]，请联系相关人员!", ibfMarketFlow.getShortMarketId()));
        }
        ibfMarketFlow.customVO2DB();
        ibfMarketFlowService.updateById(ibfMarketFlow);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "业财一体-每日填报市场流量-通过id删除")
    @ApiOperation(value = "业财一体-每日填报市场流量-通过id删除", notes = "业财一体-每日填报市场流量-通过id删除")
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        ibfMarketFlowService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "业财一体-每日填报市场流量-批量删除")
    @ApiOperation(value = "业财一体-每日填报市场流量-批量删除", notes = "业财一体-每日填报市场流量-批量删除")
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.ibfMarketFlowService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "业财一体-每日填报市场流量-通过id查询")
    @ApiOperation(value = "业财一体-每日填报市场流量-通过id查询", notes = "业财一体-每日填报市场流量-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<IbfMarketFlow> queryById(@RequestParam(name = "id", required = true) String id) {
        IbfMarketFlow ibfMarketFlow = ibfMarketFlowService.getById(id);
        if (ibfMarketFlow == null) {
            return Result.error("未找到对应数据");
        }
        ibfMarketFlow.customDB2VO();
        return Result.OK(ibfMarketFlow);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param ibfMarketFlow
     */
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IbfMarketFlow ibfMarketFlow) {
        String title = "市场流量-每日填报";
        String selections = request.getParameter("selections");
        if (oConvertUtils.isEmpty(selections) && oConvertUtils.isEmpty(request.getParameter("dateCol"))) {
            ibfMarketFlow.setDateCol("9999-12-31");
        }
        return customExportXls(request, ibfMarketFlow, IbfMarketFlow.class, title);
    }

    /**
     * 导出excel
     *
     * @param request
     */
    protected ModelAndView customExportXls(HttpServletRequest request, IbfMarketFlow object, Class<IbfMarketFlow> clazz, String title) {
        // Step.1 组装查询条件
        QueryWrapper<IbfMarketFlow> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        // 过滤当前租户数据
        List<String> shortMarketIdList = Arrays.asList(StringUtils.split(sysUser.getRelTenantIds(), ','));
        if (!shortMarketIdList.isEmpty()) {
            queryWrapper.in("short_market_id", shortMarketIdList);
        }

        // Step.2 获取导出数据
        List<IbfMarketFlow> exportList = service.list(queryWrapper);

        exportList.forEach(IbfMarketFlow::customDB2VO);

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, title);
        mv.addObject(NormalExcelConstants.CLASS, clazz);
        //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
        ExportParams exportParams = new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title);
        exportParams.setImageBasePath(upLoadPath);
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
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
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return customImportExcel(request, response, IbfMarketFlow.class);
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    private Result<?> customImportExcel(HttpServletRequest request, HttpServletResponse response, Class<IbfMarketFlow> clazz) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
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
                List<IbfMarketFlow> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                Set<String> errShortMarketIdSet = new HashSet<>();
                // 校验市场，市场必须为 shortMarketId
                for (IbfMarketFlow ibfMarketFlow : list) {
                    String shortMarketId = ibfMarketFlow.getShortMarketId();
                    // 只有列表中的shortMarketId在字典中才行
                    if (!shortMarketIdList.contains(shortMarketId)) {
                        return Result.error("市场编号:【" + shortMarketId + "】不存在");
                    }
                    // 过滤当前租户数据
                    List<String> permissShortMarketIdList = Arrays.asList(StringUtils.split(sysUser.getRelTenantIds()));
                    if (!permissShortMarketIdList.contains(shortMarketId)) {
                        errShortMarketIdSet.add(ibfMarketFlow.getShortMarketId());
                    }
                }

                // 如果有错误的市场编号，返回错误消息
                if (!errShortMarketIdSet.isEmpty()) {
                    return Result.error("无操作权限的市场编号:【" + StringUtils.join(errShortMarketIdSet, ",") + "】，请检查权限!");
                }

                // 二元组唯一性校验，shortMarketId，monthCol
                for (IbfMarketFlow ibfMarketFlow : list) {
                    String shortMarketId = ibfMarketFlow.getShortMarketId();
                    String dateCol = ibfMarketFlow.getDateCol();
                    // 校验唯一性
                    List<IbfMarketFlow> ibfMarketFlowList = service.list(new QueryWrapper<IbfMarketFlow>()
                            .eq("short_market_id", shortMarketId)
                            .eq("date_col", dateCol)
                            .last("limit 1")
                    );
                    if (!ibfMarketFlowList.isEmpty()) {
                        ibfMarketFlow.setId(ibfMarketFlowList.get(0).getId());
                    }
                }

                list.forEach(IbfMarketFlow::customVO2DB);
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
                    log.error("文件导入失败: ", e);
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

    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:add")
    @ApiOperation(value = "业财一体-每日填报市场流量-唯一性校验", notes = "业财一体-每日填报市场流量-唯一性校验")
    @GetMapping(value = "/checkUnique")
    public Result<IbfMarketFlow> checkUnique(@RequestParam(name = "businessVersion", required = true) String businessVersion,
                                             @RequestParam(name = "shortMarketId", required = true) String shortMarketId,
                                             @RequestParam(name = "dateCol", required = true) String dateCol) {
        log.info("Received parameters -, shortMarketId: {}, dateCol: {}", shortMarketId, dateCol);
        IbfMarketFlow ibfMarketFlow = ibfMarketFlowService.checkUnique(shortMarketId, dateCol);
        if (ibfMarketFlow == null) {
            return Result.OK(null);
        }
        ibfMarketFlow.customDB2VO();
        return Result.OK(ibfMarketFlow);
    }

}
