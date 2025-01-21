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

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ibf.entity.IbfMarketFlowSys;
import org.jeecg.modules.ibf.service.IIbfMarketFlowSysService;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: ibf_market_flow_sys
 * @Author: jeecg-boot
 * @Date:   2025-01-17
 * @Version: V1.0
 */
@Api(tags="ibf_market_flow_sys")
@RestController
@RequestMapping("/ibf/ibfMarketFlowSys")
@Slf4j
public class IbfMarketFlowSysController extends JeecgController<IbfMarketFlowSys, IIbfMarketFlowSysService> {
	@Autowired
	private IIbfMarketFlowSysService ibfMarketFlowSysService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ibfMarketFlowSys
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "ibf_market_flow_sys-分页列表查询")
	@ApiOperation(value="ibf_market_flow_sys-分页列表查询", notes="ibf_market_flow_sys-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<IbfMarketFlowSys>> queryPageList(IbfMarketFlowSys ibfMarketFlowSys,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<IbfMarketFlowSys> queryWrapper = QueryGenerator.initQueryWrapper(ibfMarketFlowSys, req.getParameterMap());
		// 直接获取当前用户
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (StringUtils.isNotBlank(loginUser.getRelTenantIds())) {
			queryWrapper.in("short_market_id", Arrays.asList(StringUtils.split(loginUser.getRelTenantIds(), ',')));
		}
		Page<IbfMarketFlowSys> page = new Page<IbfMarketFlowSys>(pageNo, pageSize);
		IPage<IbfMarketFlowSys> pageList = ibfMarketFlowSysService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ibfMarketFlowSys
	 * @return
	 */
	@AutoLog(value = "ibf_market_flow_sys-添加")
	@ApiOperation(value="ibf_market_flow_sys-添加", notes="ibf_market_flow_sys-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow_sys:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody IbfMarketFlowSys ibfMarketFlowSys) {
		// 直接获取当前用户
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> shortMarketIdList = Arrays.asList(StringUtils.split(loginUser.getRelTenantIds(), ','));
		if (!shortMarketIdList.contains(ibfMarketFlowSys.getShortMarketId())) {
			return Result.ok(String.format("没有权限添加市场编号为: [%s]，请联系相关人员!", ibfMarketFlowSys.getShortMarketId()));
		}
		ibfMarketFlowSysService.save(ibfMarketFlowSys);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ibfMarketFlowSys
	 * @return
	 */
	@AutoLog(value = "ibf_market_flow_sys-编辑")
	@ApiOperation(value="ibf_market_flow_sys-编辑", notes="ibf_market_flow_sys-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow_sys:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody IbfMarketFlowSys ibfMarketFlowSys) {
		// 直接获取当前用户
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> shortMarketIdList = Arrays.asList(StringUtils.split(loginUser.getRelTenantIds(), ','));
		if (StringUtils.isNotBlank(ibfMarketFlowSys.getShortMarketId()) && !shortMarketIdList.contains(ibfMarketFlowSys.getShortMarketId())) {
			return Result.ok(String.format("没有权限修改市场编号为: [%s]，请联系相关人员!", ibfMarketFlowSys.getShortMarketId()));
		}
		ibfMarketFlowSysService.updateById(ibfMarketFlowSys);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ibf_market_flow_sys-通过id删除")
	@ApiOperation(value="ibf_market_flow_sys-通过id删除", notes="ibf_market_flow_sys-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow_sys:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		ibfMarketFlowSysService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "ibf_market_flow_sys-批量删除")
	@ApiOperation(value="ibf_market_flow_sys-批量删除", notes="ibf_market_flow_sys-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow_sys:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ibfMarketFlowSysService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "ibf_market_flow_sys-通过id查询")
	@ApiOperation(value="ibf_market_flow_sys-通过id查询", notes="ibf_market_flow_sys-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<IbfMarketFlowSys> queryById(@RequestParam(name="id",required=true) String id) {
		IbfMarketFlowSys ibfMarketFlowSys = ibfMarketFlowSysService.getById(id);
		if(ibfMarketFlowSys==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ibfMarketFlowSys);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ibfMarketFlowSys
    */
    //@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow_sys:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IbfMarketFlowSys ibfMarketFlowSys) {
		String title = "每月流量填报";
		return super.exportXls(request, ibfMarketFlowSys, IbfMarketFlowSys.class, title);
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ibf_market_flow_sys:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, IbfMarketFlowSys.class);
    }

}
