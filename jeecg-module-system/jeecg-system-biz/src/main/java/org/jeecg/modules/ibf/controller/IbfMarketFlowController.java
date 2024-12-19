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
import org.jeecg.modules.ibf.entity.IbfMarketFlow;
import org.jeecg.modules.ibf.service.IIbfMarketFlowService;

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
 * @Description: 业财一体-每日填报市场流量
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
@Api(tags="业财一体-每日填报市场流量")
@RestController
@RequestMapping("/ibf/ibfMarketFlow")
@Slf4j
public class IbfMarketFlowController extends JeecgController<IbfMarketFlow, IIbfMarketFlowService> {
	@Autowired
	private IIbfMarketFlowService ibfMarketFlowService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ibfMarketFlow
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "业财一体-每日填报市场流量-分页列表查询")
	@ApiOperation(value="业财一体-每日填报市场流量-分页列表查询", notes="业财一体-每日填报市场流量-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<IbfMarketFlow>> queryPageList(IbfMarketFlow ibfMarketFlow,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<IbfMarketFlow> queryWrapper = QueryGenerator.initQueryWrapper(ibfMarketFlow, req.getParameterMap());
		Page<IbfMarketFlow> page = new Page<IbfMarketFlow>(pageNo, pageSize);
		IPage<IbfMarketFlow> pageList = ibfMarketFlowService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ibfMarketFlow
	 * @return
	 */
	@AutoLog(value = "业财一体-每日填报市场流量-添加")
	@ApiOperation(value="业财一体-每日填报市场流量-添加", notes="业财一体-每日填报市场流量-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody IbfMarketFlow ibfMarketFlow) {
		ibfMarketFlowService.save(ibfMarketFlow);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ibfMarketFlow
	 * @return
	 */
	@AutoLog(value = "业财一体-每日填报市场流量-编辑")
	@ApiOperation(value="业财一体-每日填报市场流量-编辑", notes="业财一体-每日填报市场流量-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody IbfMarketFlow ibfMarketFlow) {
		ibfMarketFlowService.updateById(ibfMarketFlow);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "业财一体-每日填报市场流量-通过id删除")
	@ApiOperation(value="业财一体-每日填报市场流量-通过id删除", notes="业财一体-每日填报市场流量-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		ibfMarketFlowService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "业财一体-每日填报市场流量-批量删除")
	@ApiOperation(value="业财一体-每日填报市场流量-批量删除", notes="业财一体-每日填报市场流量-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
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
	@ApiOperation(value="业财一体-每日填报市场流量-通过id查询", notes="业财一体-每日填报市场流量-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<IbfMarketFlow> queryById(@RequestParam(name="id",required=true) String id) {
		IbfMarketFlow ibfMarketFlow = ibfMarketFlowService.getById(id);
		if(ibfMarketFlow==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ibfMarketFlow);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ibfMarketFlow
    */
    //@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IbfMarketFlow ibfMarketFlow) {
        return super.exportXls(request, ibfMarketFlow, IbfMarketFlow.class, "业财一体-每日填报市场流量");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ibf_market_flow:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, IbfMarketFlow.class);
    }

}
