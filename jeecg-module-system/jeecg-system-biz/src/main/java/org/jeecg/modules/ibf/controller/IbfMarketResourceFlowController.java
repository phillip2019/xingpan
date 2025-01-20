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
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ibf.entity.IbfMarketResource;
import org.jeecg.modules.ibf.entity.IbfMarketResourceFlow;
import org.jeecg.modules.ibf.service.IIbfMarketResourceFlowService;

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
 * @Description: 月市场资源-市场流量
 * @Author: jeecg-boot
 * @Date:   2025-01-15
 * @Version: V1.0
 */
@Api(tags="月市场资源-市场流量")
@RestController
@RequestMapping("/ibf/ibfMarketResourceFlow")
@Slf4j
public class IbfMarketResourceFlowController extends CustomController<IbfMarketResourceFlow, IIbfMarketResourceFlowService> {
	@Autowired
	private IIbfMarketResourceFlowService ibfMarketResourceFlowService;

	 @Lazy
	 @Autowired
	 private CommonAPI commonApi;


	 @Value("${jeecg.path.upload}")
	 private String upLoadPath;

	 public static final String DICT_CODE = "short_market_id";

	/**
	 * 分页列表查询
	 *
	 * @param ibfMarketResourceFlow
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "月市场资源-市场流量-分页列表查询")
	@ApiOperation(value="月市场资源-市场流量-分页列表查询", notes="月市场资源-市场流量-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<IbfMarketResourceFlow>> queryPageList(IbfMarketResourceFlow ibfMarketResourceFlow,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<IbfMarketResourceFlow> queryWrapper = QueryGenerator.initQueryWrapper(ibfMarketResourceFlow, req.getParameterMap());
		// 塞入市场信息
		// 直接获取当前用户
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (org.apache.commons.lang.StringUtils.isNotBlank(loginUser.getRelTenantIds())) {
			queryWrapper.in("short_market_id", Arrays.asList(org.apache.commons.lang.StringUtils.split(loginUser.getRelTenantIds(), ',')));
		}
		Page<IbfMarketResourceFlow> page = new Page<IbfMarketResourceFlow>(pageNo, pageSize);
		IPage<IbfMarketResourceFlow> pageList = ibfMarketResourceFlowService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ibfMarketResourceFlow
	 * @return
	 */
	@AutoLog(value = "月市场资源-市场流量-添加")
	@ApiOperation(value="月市场资源-市场流量-添加", notes="月市场资源-市场流量-添加")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_flow:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody IbfMarketResourceFlow ibfMarketResourceFlow) {
		// 直接获取当前用户
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> shortMarketIdList = Arrays.asList(org.apache.commons.lang.StringUtils.split(loginUser.getRelTenantIds()));
		if (!shortMarketIdList.contains(ibfMarketResourceFlow.getShortMarketId())) {
			return Result.ok(String.format("无法添加市场编号为: [%s]，请联系相关人员!", ibfMarketResourceFlow.getShortMarketId()));
		}
		ibfMarketResourceFlowService.save(ibfMarketResourceFlow);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ibfMarketResourceFlow
	 * @return
	 */
	@AutoLog(value = "月市场资源-市场流量-编辑")
	@ApiOperation(value="月市场资源-市场流量-编辑", notes="月市场资源-市场流量-编辑")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_flow:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody IbfMarketResourceFlow ibfMarketResourceFlow) {
		// 直接获取当前用户
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> shortMarketIdList = Arrays.asList(org.apache.commons.lang.StringUtils.split(loginUser.getRelTenantIds(), ','));
		if (org.apache.commons.lang.StringUtils.isNotBlank(ibfMarketResourceFlow.getShortMarketId()) && !shortMarketIdList.contains(ibfMarketResourceFlow.getShortMarketId())) {
			return Result.ok(String.format("无法修改市场编号为: [%s]，请联系相关人员!", ibfMarketResourceFlow.getShortMarketId()));
		}
		ibfMarketResourceFlowService.updateById(ibfMarketResourceFlow);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "月市场资源-市场流量-通过id删除")
	@ApiOperation(value="月市场资源-市场流量-通过id删除", notes="月市场资源-市场流量-通过id删除")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_flow:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		ibfMarketResourceFlowService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "月市场资源-市场流量-批量删除")
	@ApiOperation(value="月市场资源-市场流量-批量删除", notes="月市场资源-市场流量-批量删除")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_flow:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ibfMarketResourceFlowService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "月市场资源-市场流量-通过id查询")
	@ApiOperation(value="月市场资源-市场流量-通过id查询", notes="月市场资源-市场流量-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<IbfMarketResourceFlow> queryById(@RequestParam(name="id",required=true) String id) {
		IbfMarketResourceFlow ibfMarketResourceFlow = ibfMarketResourceFlowService.getById(id);
		if(ibfMarketResourceFlow == null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ibfMarketResourceFlow);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ibfMarketResourceFlow
    */
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_flow:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IbfMarketResourceFlow ibfMarketResourceFlow) {
		String selections = request.getParameter("selections");
		if (oConvertUtils.isEmpty(selections) && oConvertUtils.isEmpty(request.getParameter("monthCol"))) {
			ibfMarketResourceFlow.setMonthCol("9999-12");
		}
		return super.exportXls(request, ibfMarketResourceFlow, IbfMarketResourceFlow.class, "月市场资源-市场流量");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource_flow:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, IbfMarketResourceFlow.class);
    }

	 @ApiOperation(value = "月市场资源-市场流量--唯一性校验", notes = "月市场资源-市场流量-唯一性校验")
	 @GetMapping(value = "/checkUnique")
	 public Result<IbfMarketResourceFlow> checkUnique(@RequestParam(name = "shortMarketId", required = true) String shortMarketId,
												  @RequestParam(name = "monthCol", required = true) String monthCol) {
		 IbfMarketResourceFlow ibfMarketResourceFlow = ibfMarketResourceFlowService.checkUnique(shortMarketId, monthCol);
		 if (ibfMarketResourceFlow == null) {
			 return Result.OK(null);
		 }
		 return Result.OK(ibfMarketResourceFlow);
	 }

}
