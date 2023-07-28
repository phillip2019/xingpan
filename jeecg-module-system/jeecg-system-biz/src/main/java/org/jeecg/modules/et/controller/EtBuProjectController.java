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
import org.jeecg.modules.et.entity.EtBuProject;
import org.jeecg.modules.et.service.IEtBuProjectService;

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
 * @Description: et_bu_project
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Api(tags="et_bu_project")
@RestController
@RequestMapping("/et/etBuProject")
@Slf4j
public class EtBuProjectController extends JeecgController<EtBuProject, IEtBuProjectService> {
	@Autowired
	private IEtBuProjectService etBuProjectService;
	
	/**
	 * 分页列表查询
	 *
	 * @param etBuProject
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "et_bu_project-分页列表查询")
	@ApiOperation(value="et_bu_project-分页列表查询", notes="et_bu_project-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<EtBuProject>> queryPageList(EtBuProject etBuProject,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<EtBuProject> queryWrapper = QueryGenerator.initQueryWrapper(etBuProject, req.getParameterMap());
		Page<EtBuProject> page = new Page<EtBuProject>(pageNo, pageSize);
		IPage<EtBuProject> pageList = etBuProjectService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param etBuProject
	 * @return
	 */
	@AutoLog(value = "et_bu_project-添加")
	@ApiOperation(value="et_bu_project-添加", notes="et_bu_project-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:et_bu_project:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody EtBuProject etBuProject) {
		etBuProjectService.save(etBuProject);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param etBuProject
	 * @return
	 */
	@AutoLog(value = "et_bu_project-编辑")
	@ApiOperation(value="et_bu_project-编辑", notes="et_bu_project-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:et_bu_project:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody EtBuProject etBuProject) {
		etBuProjectService.updateById(etBuProject);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "et_bu_project-通过id删除")
	@ApiOperation(value="et_bu_project-通过id删除", notes="et_bu_project-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:et_bu_project:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		etBuProjectService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "et_bu_project-批量删除")
	@ApiOperation(value="et_bu_project-批量删除", notes="et_bu_project-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:et_bu_project:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.etBuProjectService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "et_bu_project-通过id查询")
	@ApiOperation(value="et_bu_project-通过id查询", notes="et_bu_project-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<EtBuProject> queryById(@RequestParam(name="id",required=true) String id) {
		EtBuProject etBuProject = etBuProjectService.getById(id);
		if(etBuProject==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(etBuProject);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param etBuProject
    */
    //@RequiresPermissions("org.jeecg.modules.demo:et_bu_project:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EtBuProject etBuProject) {
        return super.exportXls(request, etBuProject, EtBuProject.class, "et_bu_project");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("et_bu_project:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, EtBuProject.class);
    }

}
