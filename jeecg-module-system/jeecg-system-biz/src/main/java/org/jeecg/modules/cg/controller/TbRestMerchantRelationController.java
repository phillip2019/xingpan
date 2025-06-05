package org.jeecg.modules.cg.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.cg.entity.TbRestMerchantRelation;
import org.jeecg.modules.cg.service.ITbRestMerchantRelationService;

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
 * @Description: 档口食堂关系
 * @Author: jeecg-boot
 * @Date:   2025-06-05
 * @Version: V1.0
 */
@Api(tags="档口食堂关系")
@RestController
@RequestMapping("/tb/tbRestMerchantRelation")
@Slf4j
public class TbRestMerchantRelationController extends JeecgController<TbRestMerchantRelation, ITbRestMerchantRelationService> {
	@Autowired
	private ITbRestMerchantRelationService tbRestMerchantRelationService;
	
	/**
	 * 分页列表查询
	 *
	 * @param tbRestMerchantRelation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "档口食堂关系-分页列表查询")
	@ApiOperation(value="档口食堂关系-分页列表查询", notes="档口食堂关系-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TbRestMerchantRelation>> queryPageList(TbRestMerchantRelation tbRestMerchantRelation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TbRestMerchantRelation> queryWrapper = QueryGenerator.initQueryWrapper(tbRestMerchantRelation, req.getParameterMap());
		Page<TbRestMerchantRelation> page = new Page<TbRestMerchantRelation>(pageNo, pageSize);
		IPage<TbRestMerchantRelation> pageList = tbRestMerchantRelationService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param tbRestMerchantRelation
	 * @return
	 */
	@AutoLog(value = "档口食堂关系-添加")
	@ApiOperation(value="档口食堂关系-添加", notes="档口食堂关系-添加")
	@RequiresPermissions("org.jeecg.modules.demo:tb_rest_merchant_relation:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TbRestMerchantRelation tbRestMerchantRelation) {
		tbRestMerchantRelationService.save(tbRestMerchantRelation);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param tbRestMerchantRelation
	 * @return
	 */
	@AutoLog(value = "档口食堂关系-编辑")
	@ApiOperation(value="档口食堂关系-编辑", notes="档口食堂关系-编辑")
	@RequiresPermissions("org.jeecg.modules.demo:tb_rest_merchant_relation:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TbRestMerchantRelation tbRestMerchantRelation) {
		tbRestMerchantRelationService.updateById(tbRestMerchantRelation);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "档口食堂关系-通过id删除")
	@ApiOperation(value="档口食堂关系-通过id删除", notes="档口食堂关系-通过id删除")
	@RequiresPermissions("org.jeecg.modules.demo:tb_rest_merchant_relation:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		tbRestMerchantRelationService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "档口食堂关系-批量删除")
	@ApiOperation(value="档口食堂关系-批量删除", notes="档口食堂关系-批量删除")
	@RequiresPermissions("org.jeecg.modules.demo:tb_rest_merchant_relation:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tbRestMerchantRelationService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "档口食堂关系-通过id查询")
	@ApiOperation(value="档口食堂关系-通过id查询", notes="档口食堂关系-通过id查询")
	@RequiresPermissions("org.jeecg.modules.demo:tb_rest_merchant_relation:queryById")
	@GetMapping(value = "/queryById")
	public Result<TbRestMerchantRelation> queryById(@RequestParam(name="id",required=true) String id) {
		TbRestMerchantRelation tbRestMerchantRelation = tbRestMerchantRelationService.getById(id);
		if(tbRestMerchantRelation==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(tbRestMerchantRelation);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param tbRestMerchantRelation
    */
    @RequiresPermissions("org.jeecg.modules.demo:tb_rest_merchant_relation:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TbRestMerchantRelation tbRestMerchantRelation) {
        return super.exportXls(request, tbRestMerchantRelation, TbRestMerchantRelation.class, "档口食堂关系");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("tb_rest_merchant_relation:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TbRestMerchantRelation.class);
    }

}
