package org.jeecg.modules.ibf.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ibf.entity.IbfReportingSummary;
import org.jeecg.modules.ibf.service.IIbfReportingSummaryService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.ibf.util.IbfDateUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 填报发布汇总
 * @Author: jeecg-boot
 * @Date:   2025-01-17
 * @Version: V1.0
 */
@Api(tags="填报发布汇总")
@RestController
@RequestMapping("/ibf/ibfReportingSummary")
@Slf4j
public class IbfReportingSummaryController extends JeecgController<IbfReportingSummary, IIbfReportingSummaryService> {
	@Autowired
	private IIbfReportingSummaryService ibfReportingSummaryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ibfReportingSummary
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "填报发布汇总-分页列表查询")
	@ApiOperation(value="填报发布汇总-分页列表查询", notes="填报发布汇总-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<IbfReportingSummary>> queryPageList(IbfReportingSummary ibfReportingSummary,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<IbfReportingSummary> queryWrapper = QueryGenerator.initQueryWrapper(ibfReportingSummary, req.getParameterMap());
		Page<IbfReportingSummary> page = new Page<IbfReportingSummary>(pageNo, pageSize);
		IPage<IbfReportingSummary> pageList = ibfReportingSummaryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ibfReportingSummary
	 * @return
	 */
	@AutoLog(value = "填报发布汇总-添加")
	@ApiOperation(value="填报发布汇总-添加", notes="填报发布汇总-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_reporting_summary:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody IbfReportingSummary ibfReportingSummary) {
		ibfReportingSummaryService.save(ibfReportingSummary);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ibfReportingSummary
	 * @return
	 */
	@AutoLog(value = "填报发布汇总-编辑")
	@ApiOperation(value="填报发布汇总-编辑", notes="填报发布汇总-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_reporting_summary:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody IbfReportingSummary ibfReportingSummary) {
		ibfReportingSummaryService.updateById(ibfReportingSummary);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "填报发布汇总-通过id删除")
	@ApiOperation(value="填报发布汇总-通过id删除", notes="填报发布汇总-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_reporting_summary:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		ibfReportingSummaryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "填报发布汇总-批量删除")
	@ApiOperation(value="填报发布汇总-批量删除", notes="填报发布汇总-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_reporting_summary:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ibfReportingSummaryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "填报发布汇总-通过id查询")
	@ApiOperation(value="填报发布汇总-通过id查询", notes="填报发布汇总-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<IbfReportingSummary> queryById(@RequestParam(name="id",required=true) String id) {
		IbfReportingSummary ibfReportingSummary = ibfReportingSummaryService.getById(id);
		if(ibfReportingSummary==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ibfReportingSummary);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ibfReportingSummary
    */
    //@RequiresPermissions("org.jeecg.modules.demo:ibf_reporting_summary:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IbfReportingSummary ibfReportingSummary) {
        return super.exportXls(request, ibfReportingSummary, IbfReportingSummary.class, "填报发布汇总");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ibf_reporting_summary:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, IbfReportingSummary.class);
    }

	// 添加获取当前所属月份的接口
	 @GetMapping(value = "/getCurrentMonth")
	 public Result<String> getCurrentMonth() {
		 String currentMonth = IbfDateUtil.getCurrentMonth();
		 return Result.OK(currentMonth);
	 }

}
