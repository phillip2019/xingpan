package org.jeecg.modules.ibf.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javassist.tools.reflect.CannotInvokeException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ibf.entity.IbfMarketFinance;
import org.jeecg.modules.ibf.service.IIbfMarketFinanceService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;


/**
 * @Description: 业财一体-财务填报
 * @Author: jeecg-boot
 * @Date: 2024-12-19
 * @Version: V1.0
 */
@Api(tags = "业财一体-财务填报")
@RestController
@RequestMapping("/ibf/ibfMarketFinance")
@Slf4j
public class IbfMarketFinanceController extends JeecgController<IbfMarketFinance, IIbfMarketFinanceService> {
    @Autowired
    private IIbfMarketFinanceService ibfMarketFinanceService;

    @Lazy
    @Autowired
    private CommonAPI commonApi;

    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    public static final String DICT_CODE = "finance_short_market_id";

    /**
     * 分页列表查询
     *
     * @param ibfMarketFinance
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "业财一体-财务填报-分页列表查询")
    @ApiOperation(value = "业财一体-财务填报-分页列表查询", notes = "业财一体-财务填报-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<IbfMarketFinance>> queryPageList(IbfMarketFinance ibfMarketFinance,
                                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                         HttpServletRequest req) {
        QueryWrapper<IbfMarketFinance> queryWrapper = QueryGenerator.initQueryWrapper(ibfMarketFinance, req.getParameterMap());
        // 塞入市场信息
        // 直接获取当前用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isNotBlank(loginUser.getRelTenantIds())) {
            queryWrapper.in("short_market_id", Arrays.asList(StringUtils.split(loginUser.getRelTenantIds())));
        }
        Page<IbfMarketFinance> page = new Page<IbfMarketFinance>(pageNo, pageSize);
        IPage<IbfMarketFinance> pageList = ibfMarketFinanceService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param ibfMarketFinance
     * @return
     */
    @AutoLog(value = "业财一体-财务填报-添加")
    @ApiOperation(value = "业财一体-财务填报-添加", notes = "业财一体-财务填报-添加")
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_finance:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody IbfMarketFinance ibfMarketFinance) {
        // 直接获取当前用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> shortMarketIdList = Arrays.asList(StringUtils.split(loginUser.getRelTenantIds()));
        if (!shortMarketIdList.contains(ibfMarketFinance.getShortMarketId())) {
            return Result.ok(String.format("无法添加市场编号为: [%s]，请联系相关人员!", ibfMarketFinance.getShortMarketId()));
        }
        ibfMarketFinanceService.save(ibfMarketFinance);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param ibfMarketFinance
     * @return
     */
    @AutoLog(value = "业财一体-财务填报-编辑")
    @ApiOperation(value = "业财一体-财务填报-编辑", notes = "业财一体-财务填报-编辑")
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_finance:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody IbfMarketFinance ibfMarketFinance) {
        // 直接获取当前用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> shortMarketIdList = Arrays.asList(StringUtils.split(loginUser.getRelTenantIds()));
        if (StringUtils.isNotBlank(ibfMarketFinance.getShortMarketId()) && !shortMarketIdList.contains(ibfMarketFinance.getShortMarketId())) {
            return Result.ok(String.format("无法修改市场编号为: [%s]，请联系相关人员!", ibfMarketFinance.getShortMarketId()));
        }
        ibfMarketFinanceService.updateById(ibfMarketFinance);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "业财一体-财务填报-通过id删除")
    @ApiOperation(value = "业财一体-财务填报-通过id删除", notes = "业财一体-财务填报-通过id删除")
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_finance:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        ibfMarketFinanceService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "业财一体-财务填报-批量删除")
    @ApiOperation(value = "业财一体-财务填报-批量删除", notes = "业财一体-财务填报-批量删除")
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_finance:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.ibfMarketFinanceService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "业财一体-财务填报-通过id查询")
    @ApiOperation(value = "业财一体-财务填报-通过id查询", notes = "业财一体-财务填报-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<IbfMarketFinance> queryById(@RequestParam(name = "id", required = true) String id) {
        IbfMarketFinance ibfMarketFinance = ibfMarketFinanceService.getById(id);
        if (ibfMarketFinance == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(ibfMarketFinance);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param ibfMarketFinance
     */
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_finance:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IbfMarketFinance ibfMarketFinance) {
        // 从url中获取businessVersion参数
        String businessVersion = request.getParameter("businessVersion");
        // 根据businessVersion版本参数判断，返回不同的excel模板
        String title = "财务看板(BOSS)-每月填报";
        if (businessVersion.equals("BOSS")) {
            title = "财务看板(BOSS)-每月填报";
        }
        String selections = request.getParameter("selections");

        if (oConvertUtils.isEmpty(selections) && oConvertUtils.isEmpty(request.getParameter("monthCol"))) {
            ibfMarketFinance.setMonthCol("9999-12");
        }
        return customExportXls(request, ibfMarketFinance, IbfMarketFinance.class, title);
    }

    /**
     * 导出excel
     *
     * @param request
     */
    protected ModelAndView customExportXls(HttpServletRequest request, IbfMarketFinance object, Class<IbfMarketFinance> clazz, String title) {
        // Step.1 组装查询条件
        QueryWrapper<IbfMarketFinance> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id",selectionList);
        }
        // 过滤当前租户数据
        List<String> shortMarketIdList = Arrays.asList(StringUtils.split(sysUser.getRelTenantIds()));
        if (!shortMarketIdList.isEmpty()) {
            queryWrapper.in("short_market_id", shortMarketIdList);
        }

        // Step.2 获取导出数据
        List<IbfMarketFinance> exportList = service.list(queryWrapper);

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
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_finance:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        // 从url中获取businessVersion参数
        String businessVersion = request.getParameter("businessVersion");
        // 只支持BOSS、OPERATION、FINANCE参数
        if (oConvertUtils.isEmpty(businessVersion)) {
            return Result.error("业务版本不能为空");
        }
        if (!businessVersion.equals("BOSS") && !businessVersion.equals("OPERATION") && !businessVersion.equals("FINANCE")) {
            return Result.error("业务版本参数错误");
        }
        return customImportExcel(request, response, IbfMarketFinance.class, businessVersion);
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    private Result<?> customImportExcel(HttpServletRequest request, HttpServletResponse response, Class<IbfMarketFinance> clazz, String businessVersion) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

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
                List<IbfMarketFinance> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                // 塞入businessVersion
                for (IbfMarketFinance ibfMarketFinance : list) {
                    ibfMarketFinance.setBusinessVersion(businessVersion);
                }

                List<IbfMarketFinance> validList = new ArrayList<>();
                // 校验市场，市场必须为 shortMarketId
                for (IbfMarketFinance ibfMarketFinance : list) {
                    String shortMarketId = ibfMarketFinance.getShortMarketId();
                    // 只有列表中的shortMarketId在字典中才行
                    if (!shortMarketIdList.contains(shortMarketId)) {
                        return Result.error("市场编号:【" + shortMarketId + "】不存在");
                    }

                    // 过滤当前租户数据
                    List<String> permissShortMarketIdList = Arrays.asList(StringUtils.split(sysUser.getRelTenantIds()));
                    if (permissShortMarketIdList.contains(shortMarketId)) {

                    }
                    // 校验月份
                    String monthCol = ibfMarketFinance.getMonthCol();
                    // 校验月份格式
                    if (StringUtils.isBlank(monthCol)) {
                        log.warn("存在月份数据为空记录, {}", ibfMarketFinance);
                        continue;
                    }
                    if (StringUtils.isBlank(monthCol) || !monthCol.matches("\\d{4}-\\d{2}")) {
                        return Result.error("月份格式错误:【" + monthCol + "】");
                    }
                    // 校验月份是否大于当前月份
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM").parse(monthCol);
                        if (date.after(now)) {
                            return Result.error("月份不能大于当前月份:【" + monthCol + "】");
                        }
                    } catch (Exception e) {
                        log.error("月份格式错误:【" + monthCol + "】", e);
                        return Result.error("月份格式错误:【" + monthCol + "】");
                    }
                    validList.add(ibfMarketFinance);
                }

                list = validList;
                // 二元组唯一性校验，businessVersion，monthCol
                for (IbfMarketFinance ibfMarketFinance : list) {
                    String monthCol = ibfMarketFinance.getMonthCol();
                    String shortMarketId = ibfMarketFinance.getShortMarketId();
                    // 校验唯一性
                    List<IbfMarketFinance> ibfMarketFinanceList = service.list(new QueryWrapper<IbfMarketFinance>()
                            .eq("short_market_id", shortMarketId)
                            .eq("business_version", businessVersion)
                            .eq("month_col", monthCol)
                            .last("limit 1")
                    );
                    if (!ibfMarketFinanceList.isEmpty()) {
                        ibfMarketFinance.setId(ibfMarketFinanceList.get(0).getId());
                    }
                }

                //update-begin-author:taoyan date:20190528 for:批量插入数据
                long start = System.currentTimeMillis();
                service.saveOrUpdateBatch(list);
                //400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
                //1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                //update-end-author:taoyan date:20190528 for:批量插入数据
                return Result.ok("文件导入成功！数据行数：" + list.size());
            } catch (InvocationTargetException e) {
                log.error("文件导入失败: ", e);
                return Result.error("文件导入失败:" + e.getTargetException().getMessage());
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

    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_finance:add")
    @ApiOperation(value = "业财一体-财务填报-唯一性校验", notes = "业财一体-财务填报-唯一性校验")
    @GetMapping(value = "/checkUnique")
    public Result<IbfMarketFinance> checkUnique(@RequestParam(name = "businessVersion", required = true) String businessVersion,
                                                @RequestParam(name = "shortMarketId", required = true) String shortMarketId,
                                                @RequestParam(name = "monthCol", required = true) String monthCol) {
        log.info("Received parameters - businessVersion: {}, shortMarketId: {}, monthCol: {}", businessVersion, shortMarketId, monthCol);
        IbfMarketFinance ibfMarketFinance = ibfMarketFinanceService.checkUnique(businessVersion, shortMarketId, monthCol);
        if (ibfMarketFinance == null) {
            return Result.OK(null);
        }
        return Result.OK(ibfMarketFinance);
    }
}
