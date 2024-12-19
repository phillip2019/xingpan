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
import org.jeecg.modules.ibf.entity.IbfMarketResource;
import org.jeecg.modules.ibf.service.IIbfMarketResourceService;

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
 * @Description: 业财一体-市场资源填报表
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
@Api(tags="业财一体-市场资源填报表")
@RestController
@RequestMapping("/ibf/ibfMarketResource")
@Slf4j
public class IbfMarketResourceController extends JeecgController<IbfMarketResource, IIbfMarketResourceService> {
	@Autowired
	private IIbfMarketResourceService ibfMarketResourceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ibfMarketResource
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "业财一体-市场资源填报表-分页列表查询")
	@ApiOperation(value="业财一体-市场资源填报表-分页列表查询", notes="业财一体-市场资源填报表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<IbfMarketResource>> queryPageList(IbfMarketResource ibfMarketResource,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<IbfMarketResource> queryWrapper = QueryGenerator.initQueryWrapper(ibfMarketResource, req.getParameterMap());
		Page<IbfMarketResource> page = new Page<IbfMarketResource>(pageNo, pageSize);
		IPage<IbfMarketResource> pageList = ibfMarketResourceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ibfMarketResource
	 * @return
	 */
	@AutoLog(value = "业财一体-市场资源填报表-添加")
	@ApiOperation(value="业财一体-市场资源填报表-添加", notes="业财一体-市场资源填报表-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody IbfMarketResource ibfMarketResource) {
		ibfMarketResourceService.save(ibfMarketResource);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ibfMarketResource
	 * @return
	 */
	@AutoLog(value = "业财一体-市场资源填报表-编辑")
	@ApiOperation(value="业财一体-市场资源填报表-编辑", notes="业财一体-市场资源填报表-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody IbfMarketResource ibfMarketResource) {
		ibfMarketResourceService.updateById(ibfMarketResource);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "业财一体-市场资源填报表-通过id删除")
	@ApiOperation(value="业财一体-市场资源填报表-通过id删除", notes="业财一体-市场资源填报表-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		ibfMarketResourceService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "业财一体-市场资源填报表-批量删除")
	@ApiOperation(value="业财一体-市场资源填报表-批量删除", notes="业财一体-市场资源填报表-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ibfMarketResourceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "业财一体-市场资源填报表-通过id查询")
	@ApiOperation(value="业财一体-市场资源填报表-通过id查询", notes="业财一体-市场资源填报表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<IbfMarketResource> queryById(@RequestParam(name="id",required=true) String id) {
		IbfMarketResource ibfMarketResource = ibfMarketResourceService.getById(id);
		if(ibfMarketResource==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ibfMarketResource);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ibfMarketResource
    */
    //@RequiresPermissions("org.jeecg.modules.demo:ibf_market_resource:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IbfMarketResource ibfMarketResource) {
        return super.exportXls(request, ibfMarketResource, IbfMarketResource.class, "业财一体-市场资源填报表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ibf_market_resource:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, IbfMarketResource.class);
    }

}
