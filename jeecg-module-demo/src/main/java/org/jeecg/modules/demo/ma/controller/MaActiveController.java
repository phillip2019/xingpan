package org.jeecg.modules.demo.ma.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.ma.entity.MaActive;
import org.jeecg.modules.demo.ma.entity.MaActiveYlbMaterial;
import org.jeecg.modules.demo.ma.service.IMaActiveService;

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
 * @Description: 活动
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
@Api(tags="活动")
@RestController
@RequestMapping("/ma/active")
@Slf4j
public class MaActiveController extends JeecgController<MaActive, IMaActiveService> {
	@Autowired
	private IMaActiveService maActiveService;
	
	/**
	 * 分页列表查询
	 *
	 * @param maActive
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "活动-分页列表查询")
	@ApiOperation(value="活动-分页列表查询", notes="活动-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MaActive>> queryPageList(MaActive maActive,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MaActive> queryWrapper = QueryGenerator.initQueryWrapper(maActive, req.getParameterMap());
		Page<MaActive> page = new Page<MaActive>(pageNo, pageSize);
		IPage<MaActive> pageList = maActiveService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param maActive
	 * @return
	 */
	@AutoLog(value = "活动-添加")
	@ApiOperation(value="活动-添加", notes="活动-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_active:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody MaActive maActive) {
		maActiveService.save(maActive);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param maActive
	 * @return
	 */
	@AutoLog(value = "活动-编辑")
	@ApiOperation(value="活动-编辑", notes="活动-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_active:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody MaActive maActive) {
		maActiveService.updateById(maActive);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "活动-通过id删除")
	@ApiOperation(value="活动-通过id删除", notes="活动-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_active:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		maActiveService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "活动-批量删除")
	@ApiOperation(value="活动-批量删除", notes="活动-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_active:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.maActiveService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "活动-通过id查询")
	@ApiOperation(value="活动-通过id查询", notes="活动-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MaActive> queryById(@RequestParam(name="id",required=true) String id) {
		MaActive maActive = maActiveService.getById(id);
		if(maActive==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(maActive);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param maActive
    */
    //@RequiresPermissions("org.jeecg.modules.demo:ma_active:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MaActive maActive) {
        return super.exportXls(request, maActive, MaActive.class, "活动");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ma_active:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MaActive.class);
    }


	 /**
	  * 通过excel导入易拉宝数据
	  * @param request
	  * @param response
	  * @return
	  */
	 //@RequiresPermissions("ma_active:importExcel")
	 @RequestMapping(value = "/importYLBExcel", method = RequestMethod.POST)
	 public Result<?> importYlbExcel(@NotNull(message = "活动编号必填") @RequestParam("id") Long activeId, HttpServletRequest request, HttpServletResponse response) {
		 return maActiveService.importYlbExcel(activeId, request, response, MaActiveYlbMaterial.class);
	 }
}
