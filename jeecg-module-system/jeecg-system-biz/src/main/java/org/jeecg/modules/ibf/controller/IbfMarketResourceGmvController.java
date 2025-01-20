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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ibf.entity.IbfMarketResource;
import org.jeecg.modules.ibf.entity.IbfMarketResourceGmv;
import org.jeecg.modules.ibf.service.IIbfMarketResourceGmvService;

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
 * @Description: 月市场资源-市场成交额填报
 * @Author: jeecg-boot
 * @Date:   2025-01-15
 * @Version: V1.0
 */
@Api(tags="月市场资源-市场成交额填报")
@RestController
@RequestMapping("/ibf/ibfMarketResourceGmv")
@Slf4j
public class IbfMarketResourceGmvController extends CustomController<IbfMarketResourceGmv, IIbfMarketResourceGmvService> {
	@Autowired
	private IIbfMarketResourceGmvService ibfMarketResourceGmvService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ibfMarketResourceGmv
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "月市场资源-市场成交额填报-分页列表查询")
	@ApiOperation(value="月市场资源-市场成交额填报-分页列表查询", notes="月市场资源-市场成交额填报-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<IbfMarketResourceGmv>> queryPageList(IbfMarketResourceGmv ibfMarketResourceGmv,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<IbfMarketResourceGmv> queryWrapper = QueryGenerator.initQueryWrapper(ibfMarketResourceGmv, req.getParameterMap());
		// 塞入市场信息
		// 直接获取当前用户
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (org.apache.commons.lang.StringUtils.isNotBlank(loginUser.getRelTenantIds())) {
			queryWrapper.in("short_market_id", Arrays.asList(org.apache.commons.lang.StringUtils.split(loginUser.getRelTenantIds(), ',')));
		}
		Page<IbfMarketResourceGmv> page = new Page<IbfMarketResourceGmv>(pageNo, pageSize);
		IPage<IbfMarketResourceGmv> pageList = ibfMarketResourceGmvService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ibfMarketResourceGmv
	 * @return
	 */
	@AutoLog(value = "月市场资源-市场成交额填报-添加")
	@ApiOperation(value="月市场资源-市场成交额填报-添加", notes="月市场资源-市场成交额填报-添加")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_gmv:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody IbfMarketResourceGmv ibfMarketResourceGmv) {
		// 直接获取当前用户
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> shortMarketIdList = Arrays.asList(org.apache.commons.lang.StringUtils.split(loginUser.getRelTenantIds()));
		if (!shortMarketIdList.contains(ibfMarketResourceGmv.getShortMarketId())) {
			return Result.ok(String.format("无法添加市场编号为: [%s]，请联系相关人员!", ibfMarketResourceGmv.getShortMarketId()));
		}
		ibfMarketResourceGmvService.save(ibfMarketResourceGmv);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ibfMarketResourceGmv
	 * @return
	 */
	@AutoLog(value = "月市场资源-市场成交额填报-编辑")
	@ApiOperation(value="月市场资源-市场成交额填报-编辑", notes="月市场资源-市场成交额填报-编辑")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_gmv:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody IbfMarketResourceGmv ibfMarketResourceGmv) {
		// 直接获取当前用户
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> shortMarketIdList = Arrays.asList(org.apache.commons.lang.StringUtils.split(loginUser.getRelTenantIds(), ','));
		if (org.apache.commons.lang.StringUtils.isNotBlank(ibfMarketResourceGmv.getShortMarketId()) && !shortMarketIdList.contains(ibfMarketResourceGmv.getShortMarketId())) {
			return Result.ok(String.format("无法修改市场编号为: [%s]，请联系相关人员!", ibfMarketResourceGmv.getShortMarketId()));
		}
		ibfMarketResourceGmvService.updateById(ibfMarketResourceGmv);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "月市场资源-市场成交额填报-通过id删除")
	@ApiOperation(value="月市场资源-市场成交额填报-通过id删除", notes="月市场资源-市场成交额填报-通过id删除")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_gmv:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		ibfMarketResourceGmvService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "月市场资源-市场成交额填报-批量删除")
	@ApiOperation(value="月市场资源-市场成交额填报-批量删除", notes="月市场资源-市场成交额填报-批量删除")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_gmv:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ibfMarketResourceGmvService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "月市场资源-市场成交额填报-通过id查询")
	@ApiOperation(value="月市场资源-市场成交额填报-通过id查询", notes="月市场资源-市场成交额填报-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<IbfMarketResourceGmv> queryById(@RequestParam(name="id",required=true) String id) {
		IbfMarketResourceGmv ibfMarketResourceGmv = ibfMarketResourceGmvService.getById(id);
		if(ibfMarketResourceGmv==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ibfMarketResourceGmv);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ibfMarketResourceGmv
    */
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_gmv:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IbfMarketResourceGmv ibfMarketResourceGmv) {
		String title = "月市场资源-市场成交额填报";
		String selections = request.getParameter("selections");
		if (oConvertUtils.isEmpty(selections) && oConvertUtils.isEmpty(request.getParameter("monthCol"))) {
			ibfMarketResourceGmv.setMonthCol("9999-12");
		}
		return super.exportXls(request, ibfMarketResourceGmv, IbfMarketResourceGmv.class, title);
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_gmv:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, IbfMarketResourceGmv.class);
    }

	 @ApiOperation(value = "月市场资源-市场成交额填报-唯一性校验", notes = "月市场资源-市场成交额填报-唯一性校验")
	 @GetMapping(value = "/checkUnique")
	 public Result<IbfMarketResourceGmv> checkUnique(@RequestParam(name = "shortMarketId", required = true) String shortMarketId,
												  @RequestParam(name = "monthCol", required = true) String monthCol) {
		 IbfMarketResourceGmv ibfMarketResourceGmv = ibfMarketResourceGmvService.checkUnique(shortMarketId, monthCol);
		 if (ibfMarketResourceGmv == null) {
			 return Result.OK(null);
		 }
		 return Result.OK(ibfMarketResourceGmv);
	 }
}
