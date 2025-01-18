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
import org.jeecg.modules.ibf.entity.IbfMarketResourceSys;
import org.jeecg.modules.ibf.service.IIbfMarketResourceSysService;

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
 * @Description: ibf_market_resource_sys
 * @Author: jeecg-boot
 * @Date:   2025-01-17
 * @Version: V1.0
 */
@Api(tags="ibf_market_resource_sys")
@RestController
@RequestMapping("/ibf/ibfMarketResourceSys")
@Slf4j
public class IbfMarketResourceSysController extends JeecgController<IbfMarketResourceSys, IIbfMarketResourceSysService> {
	@Autowired
	private IIbfMarketResourceSysService ibfMarketResourceSysService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ibfMarketResourceSys
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "ibf_market_resource_sys-分页列表查询")
	@ApiOperation(value="ibf_market_resource_sys-分页列表查询", notes="ibf_market_resource_sys-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<IbfMarketResourceSys>> queryPageList(IbfMarketResourceSys ibfMarketResourceSys,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<IbfMarketResourceSys> queryWrapper = QueryGenerator.initQueryWrapper(ibfMarketResourceSys, req.getParameterMap());
		// 直接获取当前用户
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (StringUtils.isNotBlank(loginUser.getRelTenantIds())) {
			queryWrapper.in("short_market_id", Arrays.asList(StringUtils.split(loginUser.getRelTenantIds(), ',')));
		}
		Page<IbfMarketResourceSys> page = new Page<IbfMarketResourceSys>(pageNo, pageSize);
		IPage<IbfMarketResourceSys> pageList = ibfMarketResourceSysService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ibfMarketResourceSys
	 * @return
	 */
	@AutoLog(value = "ibf_market_resource_sys-添加")
	@ApiOperation(value="ibf_market_resource_sys-添加", notes="ibf_market_resource_sys-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_sys:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody IbfMarketResourceSys ibfMarketResourceSys) {
		ibfMarketResourceSysService.save(ibfMarketResourceSys);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ibfMarketResourceSys
	 * @return
	 */
	@AutoLog(value = "ibf_market_resource_sys-编辑")
	@ApiOperation(value="ibf_market_resource_sys-编辑", notes="ibf_market_resource_sys-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_sys:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody IbfMarketResourceSys ibfMarketResourceSys) {
		ibfMarketResourceSysService.updateById(ibfMarketResourceSys);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ibf_market_resource_sys-通过id删除")
	@ApiOperation(value="ibf_market_resource_sys-通过id删除", notes="ibf_market_resource_sys-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_sys:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		ibfMarketResourceSysService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "ibf_market_resource_sys-批量删除")
	@ApiOperation(value="ibf_market_resource_sys-批量删除", notes="ibf_market_resource_sys-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_sys:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ibfMarketResourceSysService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "ibf_market_resource_sys-通过id查询")
	@ApiOperation(value="ibf_market_resource_sys-通过id查询", notes="ibf_market_resource_sys-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<IbfMarketResourceSys> queryById(@RequestParam(name="id",required=true) String id) {
		IbfMarketResourceSys ibfMarketResourceSys = ibfMarketResourceSysService.getById(id);
		if(ibfMarketResourceSys==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ibfMarketResourceSys);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ibfMarketResourceSys
    */
    //@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_sys:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IbfMarketResourceSys ibfMarketResourceSys) {
        return super.exportXls(request, ibfMarketResourceSys, IbfMarketResourceSys.class, "ibf_market_resource_sys");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ibf_market_resource_sys:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, IbfMarketResourceSys.class);
    }

}
