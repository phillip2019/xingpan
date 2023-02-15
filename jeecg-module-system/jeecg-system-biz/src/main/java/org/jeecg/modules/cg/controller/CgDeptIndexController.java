package org.jeecg.modules.cg.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.cg.entity.CgDeptIndex;
import org.jeecg.modules.cg.service.ICgDeptIndexService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: chinagoods平台部门指标清册
 * @Author: jeecg-boot
 * @Date:   2023-02-13
 * @Version: V1.0
 */
@Api(tags="chinagoods平台部门指标清册")
@RestController
@RequestMapping("/cg/cgDeptIndex")
@Slf4j
public class CgDeptIndexController extends JeecgController<CgDeptIndex, ICgDeptIndexService> {
	@Autowired
	private ICgDeptIndexService cgDeptIndexService;

	 @Autowired
	 private ISysCategoryService sysCategoryService;
	/**
	 * 分页列表查询
	 *
	 * @param cgDeptIndex
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "chinagoods平台部门指标清册-分页列表查询")
	@ApiOperation(value="chinagoods平台部门指标清册-分页列表查询", notes="chinagoods平台部门指标清册-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<CgDeptIndex>> queryPageList(CgDeptIndex cgDeptIndex,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Result<IPage<CgDeptIndex>> result = new Result<>();
		QueryWrapper<CgDeptIndex> queryWrapper = QueryGenerator.initQueryWrapper(cgDeptIndex, req.getParameterMap());
		Page<CgDeptIndex> page = new Page<CgDeptIndex>(pageNo, pageSize);
		IPage<CgDeptIndex> pageList = cgDeptIndexService.page(page, queryWrapper);

		//批量查询用户的所属部门
		//step.1 先拿到全部的 deptIds
		//step.2 通过 deptIds，一次性查询指标的所属部门名字
		List<String> deptIds = pageList.getRecords().stream().map(CgDeptIndex::getDeptId).collect(Collectors.toList());
		if(deptIds.size() > 0){
			List<SysCategory>  sysCategoryList = sysCategoryService.queryListByIds(deptIds);
			Map<String, String> deptId2DeptName = new HashMap<>(sysCategoryList.size());
			for (SysCategory category : sysCategoryList) {
				deptId2DeptName.put(category.getId(), category.getName());
			}
			pageList.getRecords().forEach(item->{
				item.setDeptText(deptId2DeptName.get(item.getDeptId()));
			});
		}
		result.setSuccess(true);
		result.setResult(pageList);

		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param cgDeptIndex
	 * @return
	 */
	@AutoLog(value = "chinagoods平台部门指标清册-添加")
	@ApiOperation(value="chinagoods平台部门指标清册-添加", notes="chinagoods平台部门指标清册-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CgDeptIndex cgDeptIndex) {
		cgDeptIndexService.save(cgDeptIndex);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cgDeptIndex
	 * @return
	 */
	@AutoLog(value = "chinagoods平台部门指标清册-编辑")
	@ApiOperation(value="chinagoods平台部门指标清册-编辑", notes="chinagoods平台部门指标清册-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CgDeptIndex cgDeptIndex) {
		cgDeptIndexService.updateById(cgDeptIndex);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "chinagoods平台部门指标清册-通过id删除")
	@ApiOperation(value="chinagoods平台部门指标清册-通过id删除", notes="chinagoods平台部门指标清册-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		cgDeptIndexService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "chinagoods平台部门指标清册-批量删除")
	@ApiOperation(value="chinagoods平台部门指标清册-批量删除", notes="chinagoods平台部门指标清册-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cgDeptIndexService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "chinagoods平台部门指标清册-通过id查询")
	@ApiOperation(value="chinagoods平台部门指标清册-通过id查询", notes="chinagoods平台部门指标清册-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<CgDeptIndex> queryById(@RequestParam(name="id",required=true) String id) {
		CgDeptIndex cgDeptIndex = cgDeptIndexService.getById(id);
		if(cgDeptIndex==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cgDeptIndex);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cgDeptIndex
    */
    //@RequiresPermissions("org.jeecg.modules.demo:cg_dept_index:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CgDeptIndex cgDeptIndex) {
        return super.exportXls(request, cgDeptIndex, CgDeptIndex.class, "chinagoods平台部门指标清册");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("cg_dept_index:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CgDeptIndex.class);
    }

}
