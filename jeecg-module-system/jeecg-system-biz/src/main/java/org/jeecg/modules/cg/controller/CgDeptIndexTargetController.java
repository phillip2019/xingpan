package org.jeecg.modules.demo.cg.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.cg.entity.CgDeptIndex;
import org.jeecg.modules.cg.entity.CgDeptIndexValue;
import org.jeecg.modules.cg.service.ICgDeptIndexService;
import org.jeecg.modules.demo.cg.entity.CgDeptIndexTarget;
import org.jeecg.modules.demo.cg.service.ICgDeptIndexTargetService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.system.service.ISysCategoryService;
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
 * @Description: cg_dept_index_target
 * @Author: jeecg-boot
 * @Date:   2023-02-19
 * @Version: V1.0
 */
@Api(tags="cg_dept_index_target")
@RestController
@RequestMapping("/cg/cgDeptIndexTarget")
@Slf4j
public class CgDeptIndexTargetController extends JeecgController<CgDeptIndexTarget, ICgDeptIndexTargetService> {
	@Autowired
	private ICgDeptIndexTargetService cgDeptIndexTargetService;

	 @Autowired
	 private ICgDeptIndexService cgDeptIndexService;

	 @Autowired
	 private ISysCategoryService sysCategoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param cgDeptIndexTarget
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "cg_dept_index_target-分页列表查询")
	@ApiOperation(value="cg_dept_index_target-分页列表查询", notes="cg_dept_index_target-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<CgDeptIndexTarget>> queryPageList(CgDeptIndexTarget cgDeptIndexTarget,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		// 若请求参数中包含部门，则先查询部门编号对应的指标编号
		List<Long> indexIdList = null;
		if (StringUtils.isNotBlank(cgDeptIndexTarget.getDeptId())) {
			Map<String, Object> queryMapParam = new HashMap<String, Object>() {{
				put("dept_id", cgDeptIndexTarget.getDeptId());
			}};
			List<CgDeptIndex> indexList = cgDeptIndexService.listByMap(queryMapParam);
			indexIdList = indexList.stream().map(CgDeptIndex::getId).collect(Collectors.toList());
		}
		Result<IPage<CgDeptIndexTarget>> result = new Result<>();
		QueryWrapper<CgDeptIndexTarget> queryWrapper = QueryGenerator.initQueryWrapper(cgDeptIndexTarget, req.getParameterMap());
		if (indexIdList != null && indexIdList.size() > 0) {
			queryWrapper.in("dept_index_id", indexIdList);
		}
		// 只保留未删除记录
		queryWrapper.eq("is_deleted", 0);
		Page<CgDeptIndexTarget> page = new Page<CgDeptIndexTarget>(pageNo, pageSize);
		IPage<CgDeptIndexTarget> pageList = cgDeptIndexTargetService.page(page, queryWrapper);

		//批量指标值的所属指标编号
		//step.1 先拿到全部的 indexIds
		//step.2 通过 deptIds，一次性查询指标的所属部门名字
		List<Long> indexIds = pageList.getRecords().stream().map(CgDeptIndexTarget::getDeptIndexId).map(Long::valueOf).collect(Collectors.toList());
		if (indexIds.size() > 0) {
			List<CgDeptIndex> cgDeptIndexList = cgDeptIndexService.listByIds(indexIds);

			List<String> deptIds = cgDeptIndexList.stream().map(CgDeptIndex::getDeptId).collect(Collectors.toList());
			List<SysCategory>  sysCategoryList = sysCategoryService.queryListByIds(deptIds);
			Map<String, String> deptId2DeptName = new HashMap<>(sysCategoryList.size());
			for (SysCategory category : sysCategoryList) {
				deptId2DeptName.put(category.getId(), category.getName());
			}

			Map<Long, CgDeptIndex> deptIndexId2DeptIndexM = new HashMap<>(cgDeptIndexList.size());
			for (CgDeptIndex deptIndex : cgDeptIndexList) {
				deptIndexId2DeptIndexM.put(deptIndex.getId(), deptIndex);
			}
			pageList.getRecords().forEach(item->{
				CgDeptIndex deptIndex = deptIndexId2DeptIndexM.get(Long.valueOf(item.getDeptIndexId()));
				item.setDeptText(deptId2DeptName.get(deptIndex.getDeptId()));
				item.setDeptId(deptIndex.getDeptId());
				item.setIndexNameZh(deptIndex.getIndexNameZh());
			});
		}
		result.setSuccess(true);
		result.setResult(pageList);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param cgDeptIndexTarget
	 * @return
	 */
	@AutoLog(value = "cg_dept_index_target-添加")
	@ApiOperation(value="cg_dept_index_target-添加", notes="cg_dept_index_target-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index_target:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CgDeptIndexTarget cgDeptIndexTarget) {
		cgDeptIndexTargetService.save(cgDeptIndexTarget);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cgDeptIndexTarget
	 * @return
	 */
	@AutoLog(value = "cg_dept_index_target-编辑")
	@ApiOperation(value="cg_dept_index_target-编辑", notes="cg_dept_index_target-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index_target:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CgDeptIndexTarget cgDeptIndexTarget) {
		cgDeptIndexTargetService.updateById(cgDeptIndexTarget);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "cg_dept_index_target-通过id删除")
	@ApiOperation(value="cg_dept_index_target-通过id删除", notes="cg_dept_index_target-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index_target:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		cgDeptIndexTargetService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "cg_dept_index_target-批量删除")
	@ApiOperation(value="cg_dept_index_target-批量删除", notes="cg_dept_index_target-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index_target:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cgDeptIndexTargetService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "cg_dept_index_target-通过id查询")
	@ApiOperation(value="cg_dept_index_target-通过id查询", notes="cg_dept_index_target-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<CgDeptIndexTarget> queryById(@RequestParam(name="id",required=true) String id) {
		CgDeptIndexTarget cgDeptIndexTarget = cgDeptIndexTargetService.getById(id);
		if(cgDeptIndexTarget==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cgDeptIndexTarget);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cgDeptIndexTarget
    */
    //@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index_target:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CgDeptIndexTarget cgDeptIndexTarget) {
        return super.exportXls(request, cgDeptIndexTarget, CgDeptIndexTarget.class, "cg_dept_index_target");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("cg_dept_index_target:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CgDeptIndexTarget.class);
    }

}
