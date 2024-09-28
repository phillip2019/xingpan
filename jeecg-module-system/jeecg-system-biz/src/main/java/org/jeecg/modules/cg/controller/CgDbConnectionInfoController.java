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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.cg.entity.CgDbConnectionInfo;
import org.jeecg.modules.cg.service.ICgDbConnectionInfoService;

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
 * @Description: CG数据库连接信息
 * @Author: jeecg-boot
 * @Date:   2024-09-26
 * @Version: V1.0
 */
@Api(tags="CG数据库连接信息")
@RestController
@RequestMapping("/cg/cgDbConnectionInfo")
@Slf4j
public class CgDbConnectionInfoController extends JeecgController<CgDbConnectionInfo, ICgDbConnectionInfoService> {
	@Autowired
	private ICgDbConnectionInfoService cgDbConnectionInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param cgDbConnectionInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "CG数据库连接信息-分页列表查询")
	@ApiOperation(value="CG数据库连接信息-分页列表查询", notes="CG数据库连接信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<CgDbConnectionInfo>> queryPageList(CgDbConnectionInfo cgDbConnectionInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CgDbConnectionInfo> queryWrapper = QueryGenerator.initQueryWrapper(cgDbConnectionInfo, req.getParameterMap());
		Page<CgDbConnectionInfo> page = new Page<>(pageNo, pageSize);
		IPage<CgDbConnectionInfo> pageList = cgDbConnectionInfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param cgDbConnectionInfo
	 * @return
	 */
	@AutoLog(value = "CG数据库连接信息-添加")
	@ApiOperation(value="CG数据库连接信息-添加", notes="CG数据库连接信息-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_db_connection_info:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CgDbConnectionInfo cgDbConnectionInfo) {
		cgDbConnectionInfoService.save(cgDbConnectionInfo);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cgDbConnectionInfo
	 * @return
	 */
	@AutoLog(value = "CG数据库连接信息-编辑")
	@ApiOperation(value="CG数据库连接信息-编辑", notes="CG数据库连接信息-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_db_connection_info:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CgDbConnectionInfo cgDbConnectionInfo) {
		cgDbConnectionInfoService.updateById(cgDbConnectionInfo);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "CG数据库连接信息-通过id删除")
	@ApiOperation(value="CG数据库连接信息-通过id删除", notes="CG数据库连接信息-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_db_connection_info:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		cgDbConnectionInfoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "CG数据库连接信息-批量删除")
	@ApiOperation(value="CG数据库连接信息-批量删除", notes="CG数据库连接信息-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_db_connection_info:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cgDbConnectionInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "CG数据库连接信息-通过id查询")
	@ApiOperation(value="CG数据库连接信息-通过id查询", notes="CG数据库连接信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<CgDbConnectionInfo> queryById(@RequestParam(name="id",required=true) String id) {
		CgDbConnectionInfo cgDbConnectionInfo = cgDbConnectionInfoService.getById(id);
		if(cgDbConnectionInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cgDbConnectionInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cgDbConnectionInfo
    */
    //@RequiresPermissions("org.jeecg.modules.demo:cg_db_connection_info:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CgDbConnectionInfo cgDbConnectionInfo) {
        return super.exportXls(request, cgDbConnectionInfo, CgDbConnectionInfo.class, "CG数据库连接信息");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("cg_db_connection_info:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CgDbConnectionInfo.class);
    }

}
