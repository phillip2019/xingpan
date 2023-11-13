package org.jeecg.modules.et.controller;

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
import org.jeecg.modules.et.entity.EtClientEventScreenshot;
import org.jeecg.modules.et.service.IEtClientEventScreenshotService;

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
 * @Description: 客户端事件表截图
 * @Author: jeecg-boot
 * @Date:   2023-11-13
 * @Version: V1.0
 */
@Api(tags="客户端事件表截图")
@RestController
@RequestMapping("/et/etClientEventScreenshot")
@Slf4j
public class EtClientEventScreenshotController extends JeecgController<EtClientEventScreenshot, IEtClientEventScreenshotService> {
	@Autowired
	private IEtClientEventScreenshotService etClientEventScreenshotService;
	
	/**
	 * 分页列表查询
	 *
	 * @param etClientEventScreenshot
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "客户端事件表截图-分页列表查询")
	@ApiOperation(value="客户端事件表截图-分页列表查询", notes="客户端事件表截图-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<EtClientEventScreenshot>> queryPageList(EtClientEventScreenshot etClientEventScreenshot,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<EtClientEventScreenshot> queryWrapper = QueryGenerator.initQueryWrapper(etClientEventScreenshot, req.getParameterMap());
		Page<EtClientEventScreenshot> page = new Page<EtClientEventScreenshot>(pageNo, pageSize);
		IPage<EtClientEventScreenshot> pageList = etClientEventScreenshotService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param etClientEventScreenshot
	 * @return
	 */
	@AutoLog(value = "客户端事件表截图-添加")
	@ApiOperation(value="客户端事件表截图-添加", notes="客户端事件表截图-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:et_client_event_screenshot:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody EtClientEventScreenshot etClientEventScreenshot) {
		etClientEventScreenshotService.save(etClientEventScreenshot);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param etClientEventScreenshot
	 * @return
	 */
	@AutoLog(value = "客户端事件表截图-编辑")
	@ApiOperation(value="客户端事件表截图-编辑", notes="客户端事件表截图-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:et_client_event_screenshot:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody EtClientEventScreenshot etClientEventScreenshot) {
		etClientEventScreenshotService.updateById(etClientEventScreenshot);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户端事件表截图-通过id删除")
	@ApiOperation(value="客户端事件表截图-通过id删除", notes="客户端事件表截图-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:et_client_event_screenshot:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		etClientEventScreenshotService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "客户端事件表截图-批量删除")
	@ApiOperation(value="客户端事件表截图-批量删除", notes="客户端事件表截图-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:et_client_event_screenshot:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.etClientEventScreenshotService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "客户端事件表截图-通过id查询")
	@ApiOperation(value="客户端事件表截图-通过id查询", notes="客户端事件表截图-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<EtClientEventScreenshot> queryById(@RequestParam(name="id",required=true) String id) {
		EtClientEventScreenshot etClientEventScreenshot = etClientEventScreenshotService.getById(id);
		if(etClientEventScreenshot==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(etClientEventScreenshot);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param etClientEventScreenshot
    */
    //@RequiresPermissions("org.jeecg.modules.demo:et_client_event_screenshot:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EtClientEventScreenshot etClientEventScreenshot) {
        return super.exportXls(request, etClientEventScreenshot, EtClientEventScreenshot.class, "客户端事件表截图");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("et_client_event_screenshot:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, EtClientEventScreenshot.class);
    }

}
