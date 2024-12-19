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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 业务一体-财务填报
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
@Api(tags="业务一体-财务填报")
@RestController
@RequestMapping("/ibf/ibfMarketFinance")
@Slf4j
public class IbfMarketFinanceController extends JeecgController<IbfMarketFinance, IIbfMarketFinanceService> {
	@Autowired
	private IIbfMarketFinanceService ibfMarketFinanceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ibfMarketFinance
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "业务一体-财务填报-分页列表查询")
	@ApiOperation(value="业务一体-财务填报-分页列表查询", notes="业务一体-财务填报-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<IbfMarketFinance>> queryPageList(IbfMarketFinance ibfMarketFinance,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<IbfMarketFinance> queryWrapper = QueryGenerator.initQueryWrapper(ibfMarketFinance, req.getParameterMap());
		Page<IbfMarketFinance> page = new Page<IbfMarketFinance>(pageNo, pageSize);
		IPage<IbfMarketFinance> pageList = ibfMarketFinanceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ibfMarketFinance
	 * @return
	 */
	@AutoLog(value = "业务一体-财务填报-添加")
	@ApiOperation(value="业务一体-财务填报-添加", notes="业务一体-财务填报-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_finance:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody IbfMarketFinance ibfMarketFinance) {
		ibfMarketFinanceService.save(ibfMarketFinance);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ibfMarketFinance
	 * @return
	 */
	@AutoLog(value = "业务一体-财务填报-编辑")
	@ApiOperation(value="业务一体-财务填报-编辑", notes="业务一体-财务填报-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_finance:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody IbfMarketFinance ibfMarketFinance) {
		ibfMarketFinanceService.updateById(ibfMarketFinance);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "业务一体-财务填报-通过id删除")
	@ApiOperation(value="业务一体-财务填报-通过id删除", notes="业务一体-财务填报-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_finance:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		ibfMarketFinanceService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "业务一体-财务填报-批量删除")
	@ApiOperation(value="业务一体-财务填报-批量删除", notes="业务一体-财务填报-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_finance:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ibfMarketFinanceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "业务一体-财务填报-通过id查询")
	@ApiOperation(value="业务一体-财务填报-通过id查询", notes="业务一体-财务填报-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<IbfMarketFinance> queryById(@RequestParam(name="id",required=true) String id) {
		IbfMarketFinance ibfMarketFinance = ibfMarketFinanceService.getById(id);
		if(ibfMarketFinance==null) {
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
    //@RequiresPermissions("org.jeecg.modules.demo:ibf_market_finance:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IbfMarketFinance ibfMarketFinance) {
        return super.exportXls(request, ibfMarketFinance, IbfMarketFinance.class, "业务一体-财务填报");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ibf_market_finance:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, IbfMarketFinance.class);
    }

}
