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
import org.jeecg.modules.et.entity.EtPlatformSiteCode;
import org.jeecg.modules.et.service.IEtPlatformSiteCodeService;

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
 * @Description: 平台站点编码
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Api(tags="平台站点编码")
@RestController
@RequestMapping("/et/etPlatformSiteCode")
@Slf4j
public class EtPlatformSiteCodeController extends JeecgController<EtPlatformSiteCode, IEtPlatformSiteCodeService> {
	@Autowired
	private IEtPlatformSiteCodeService etPlatformSiteCodeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param etPlatformSiteCode
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "平台站点编码-分页列表查询")
	@ApiOperation(value="平台站点编码-分页列表查询", notes="平台站点编码-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<EtPlatformSiteCode>> queryPageList(EtPlatformSiteCode etPlatformSiteCode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<EtPlatformSiteCode> queryWrapper = QueryGenerator.initQueryWrapper(etPlatformSiteCode, req.getParameterMap());
		Page<EtPlatformSiteCode> page = new Page<EtPlatformSiteCode>(pageNo, pageSize);
		IPage<EtPlatformSiteCode> pageList = etPlatformSiteCodeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param etPlatformSiteCode
	 * @return
	 */
	@AutoLog(value = "平台站点编码-添加")
	@ApiOperation(value="平台站点编码-添加", notes="平台站点编码-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:平台站点编码:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody EtPlatformSiteCode etPlatformSiteCode) {
		etPlatformSiteCodeService.save(etPlatformSiteCode);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param etPlatformSiteCode
	 * @return
	 */
	@AutoLog(value = "平台站点编码-编辑")
	@ApiOperation(value="平台站点编码-编辑", notes="平台站点编码-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:平台站点编码:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody EtPlatformSiteCode etPlatformSiteCode) {
		etPlatformSiteCodeService.updateById(etPlatformSiteCode);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "平台站点编码-通过id删除")
	@ApiOperation(value="平台站点编码-通过id删除", notes="平台站点编码-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:平台站点编码:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		etPlatformSiteCodeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "平台站点编码-批量删除")
	@ApiOperation(value="平台站点编码-批量删除", notes="平台站点编码-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:平台站点编码:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.etPlatformSiteCodeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "平台站点编码-通过id查询")
	@ApiOperation(value="平台站点编码-通过id查询", notes="平台站点编码-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<EtPlatformSiteCode> queryById(@RequestParam(name="id",required=true) String id) {
		EtPlatformSiteCode etPlatformSiteCode = etPlatformSiteCodeService.getById(id);
		if(etPlatformSiteCode==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(etPlatformSiteCode);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param etPlatformSiteCode
    */
    //@RequiresPermissions("org.jeecg.modules.demo:平台站点编码:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EtPlatformSiteCode etPlatformSiteCode) {
        return super.exportXls(request, etPlatformSiteCode, EtPlatformSiteCode.class, "平台站点编码");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("平台站点编码:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, EtPlatformSiteCode.class);
    }

}
