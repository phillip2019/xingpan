package org.jeecg.modules.cg.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.cg.entity.CgDeptIndex;
import org.jeecg.modules.cg.entity.CgDeptIndexValue;
import org.jeecg.modules.cg.service.ICgDeptIndexService;
import org.jeecg.modules.cg.service.ICgDeptIndexValueService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.system.service.ISysCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 部门指标值
 * @Author: jeecg-boot
 * @Date:   2023-02-14
 * @Version: V1.0
 */
@Api(tags="部门指标值")
@RestController
@RequestMapping("/cg/cgDeptIndexValue")
@Slf4j
public class CgDeptIndexValueController extends JeecgController<CgDeptIndexValue, ICgDeptIndexValueService> {
	@Autowired
	private ICgDeptIndexValueService cgDeptIndexValueService;

	 @Autowired
	 private ICgDeptIndexService cgDeptIndexService;

	 @Autowired
	 private ISysCategoryService sysCategoryService;

	/**
	 * 分页列表查询
	 *
	 * @param cgDeptIndexValue
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "部门指标值-分页列表查询")
	@ApiOperation(value="部门指标值-分页列表查询", notes="部门指标值-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<CgDeptIndexValue>> queryPageList(CgDeptIndexValue cgDeptIndexValue,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		// 若请求参数中包含部门，则先查询部门编号对应的指标编号
		List<String> indexIdList = null;
		if (StringUtils.isNotBlank(cgDeptIndexValue.getDeptId())) {
			Map<String, Object> queryMapParam = new HashMap<String, Object>() {{
				put("dept_id", cgDeptIndexValue.getDeptId());
			}};
			List<CgDeptIndex> indexList = cgDeptIndexService.listByMap(queryMapParam);
			indexIdList = indexList.stream().map(CgDeptIndex::getId).collect(Collectors.toList());
		}
		Result<IPage<CgDeptIndexValue>> result = new Result<>();
		QueryWrapper<CgDeptIndexValue> queryWrapper = QueryGenerator.initQueryWrapper(cgDeptIndexValue, req.getParameterMap());
		if (indexIdList != null && indexIdList.size() > 0) {
			queryWrapper.in("dept_index_id", indexIdList);
		}
		Page<CgDeptIndexValue> page = new Page<CgDeptIndexValue>(pageNo, pageSize);
		IPage<CgDeptIndexValue> pageList = cgDeptIndexValueService.page(page, queryWrapper);

		//批量指标值的所属指标编号
		//step.1 先拿到全部的 indexIds
		//step.2 通过 deptIds，一次性查询指标的所属部门名字
		List<Long> indexIds = pageList.getRecords().stream().map(CgDeptIndexValue::getDeptIndexId).map(Long::valueOf).collect(Collectors.toList());
		if (indexIds.size() > 0) {
			List<CgDeptIndex> cgDeptIndexList = cgDeptIndexService.listByIds(indexIds);

			List<String> deptIds = cgDeptIndexList.stream().map(CgDeptIndex::getDeptId).collect(Collectors.toList());
			List<SysCategory>  sysCategoryList = sysCategoryService.queryListByIds(deptIds);
			Map<String, String> deptId2DeptName = new HashMap<>(sysCategoryList.size());
			for (SysCategory category : sysCategoryList) {
				deptId2DeptName.put(category.getId(), category.getName());
			}

			Map<String, CgDeptIndex> deptIndexId2DeptIndexM = new HashMap<>(cgDeptIndexList.size());
			for (CgDeptIndex deptIndex : cgDeptIndexList) {
				deptIndexId2DeptIndexM.put(deptIndex.getId(), deptIndex);
			}
			pageList.getRecords().forEach(item->{
				CgDeptIndex deptIndex = deptIndexId2DeptIndexM.get(item.getDeptIndexId());
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
	 * @param cgDeptIndexValue
	 * @return
	 */
	@AutoLog(value = "部门指标值-添加")
	@ApiOperation(value="部门指标值-添加", notes="部门指标值-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index_value:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CgDeptIndexValue cgDeptIndexValue) {
		// 若周期内指标存在，则更新周期内指标
		LambdaQueryWrapper<CgDeptIndexValue> queryWrapper = new LambdaQueryWrapper<CgDeptIndexValue>()
				.eq(CgDeptIndexValue::getDeptIndexId, cgDeptIndexValue.getDeptIndexId())
				.eq(CgDeptIndexValue::getBeginDate, cgDeptIndexValue.getBeginDate())
				.eq(CgDeptIndexValue::getEndDate, cgDeptIndexValue.getEndDate());
		Optional<CgDeptIndexValue> cgDeptIndexValueDBOptional = Optional.ofNullable(cgDeptIndexValueService.getOne(queryWrapper));

		cgDeptIndexValueDBOptional.ifPresent(cgDeptIndexValueDB -> {
			cgDeptIndexValue.setId(cgDeptIndexValueDB.getId())
					.setDeptIndexId(cgDeptIndexValueDB.getDeptIndexId())
					.setCreateBy(cgDeptIndexValueDB.getCreateBy())
					.setCreateTime(cgDeptIndexValueDB.getCreateTime());
		});
		cgDeptIndexValueService.saveOrUpdate(cgDeptIndexValue);
		return Result.OK("添加或更新成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cgDeptIndexValue
	 * @return
	 */
	@AutoLog(value = "部门指标值-编辑")
	@ApiOperation(value="部门指标值-编辑", notes="部门指标值-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index_value:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CgDeptIndexValue cgDeptIndexValue) {
		cgDeptIndexValueService.updateById(cgDeptIndexValue);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "部门指标值-通过id删除")
	@ApiOperation(value="部门指标值-通过id删除", notes="部门指标值-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index_value:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		cgDeptIndexValueService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "部门指标值-批量删除")
	@ApiOperation(value="部门指标值-批量删除", notes="部门指标值-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index_value:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cgDeptIndexValueService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "部门指标值-通过id查询")
	@ApiOperation(value="部门指标值-通过id查询", notes="部门指标值-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<CgDeptIndexValue> queryById(@RequestParam(name="id",required=true) String id) {
		CgDeptIndexValue cgDeptIndexValue = cgDeptIndexValueService.getById(id);
		if(cgDeptIndexValue==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cgDeptIndexValue);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cgDeptIndexValue
    */
    //@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index_value:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CgDeptIndexValue cgDeptIndexValue) {
        return super.exportXls(request, cgDeptIndexValue, CgDeptIndexValue.class, "部门指标值");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("cg_dept_index_value:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CgDeptIndexValue.class);
    }

}
