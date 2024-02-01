package org.jeecg.modules.et.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.et.entity.StandardRuleUrl;
import org.jeecg.modules.et.service.StandardRuleUrlService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 平台标准化页面名称规则
 * @Author: jeecg-boot
 * @Date:   2024-01-31
 * @Version: V1.0
 */
@Api(tags="平台标准化页面名称规则")
@RestController
@RequestMapping("/et/standardRuleUrl")
@Slf4j
public class StandardRuleUrlController extends JeecgController<StandardRuleUrl, StandardRuleUrlService> {
	@Autowired
	private StandardRuleUrlService standardRuleUrlService;
	
	/**
	 * 分页列表查询
	 *
	 * @param standardRuleUrl
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "平台标准化页面名称规则-分页列表查询")
	@ApiOperation(value="平台标准化页面名称规则-分页列表查询", notes="平台标准化页面名称规则-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StandardRuleUrl>> queryPageList(StandardRuleUrl standardRuleUrl,
														@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														HttpServletRequest req) {
		QueryWrapper<StandardRuleUrl> queryWrapper = QueryGenerator.initQueryWrapper(standardRuleUrl, req.getParameterMap());
		Page<StandardRuleUrl> page = new Page<StandardRuleUrl>(pageNo, pageSize);
		IPage<StandardRuleUrl> pageList = standardRuleUrlService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param standardRuleUrl
	 * @return
	 */
	@AutoLog(value = "平台标准化页面名称规则-添加")
	@ApiOperation(value="平台标准化页面名称规则-添加", notes="平台标准化页面名称规则-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:web_path_map_pip:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody StandardRuleUrl standardRuleUrl) {
		standardRuleUrlService.save(standardRuleUrl);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param standardRuleUrl
	 * @return
	 */
	@AutoLog(value = "平台标准化页面名称规则-编辑")
	@ApiOperation(value="平台标准化页面名称规则-编辑", notes="平台标准化页面名称规则-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:web_path_map_pip:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody StandardRuleUrl standardRuleUrl) {
		standardRuleUrlService.updateById(standardRuleUrl);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "平台标准化页面名称规则-通过id删除")
	@ApiOperation(value="平台标准化页面名称规则-通过id删除", notes="平台标准化页面名称规则-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:web_path_map_pip:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		standardRuleUrlService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "平台标准化页面名称规则-批量删除")
	@ApiOperation(value="平台标准化页面名称规则-批量删除", notes="平台标准化页面名称规则-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:web_path_map_pip:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.standardRuleUrlService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "平台标准化页面名称规则-通过id查询")
	@ApiOperation(value="平台标准化页面名称规则-通过id查询", notes="平台标准化页面名称规则-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StandardRuleUrl> queryById(@RequestParam(name="id",required=true) String id) {
		StandardRuleUrl standardRuleUrl = standardRuleUrlService.getById(id);
		if(standardRuleUrl ==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(standardRuleUrl);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param standardRuleUrl
    */
    //@RequiresPermissions("org.jeecg.modules.demo:web_path_map_pip:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StandardRuleUrl standardRuleUrl) {
        return super.exportXls(request, standardRuleUrl, StandardRuleUrl.class, "平台标准化页面名称规则");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("web_path_map_pip:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, StandardRuleUrl.class);
    }

}
