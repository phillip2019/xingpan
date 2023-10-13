package org.jeecg.modules.dqc.controller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
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
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.dqc.entity.RuleAlterInf;
import org.jeecg.modules.dqc.service.IRuleAlterInfService;

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
 * @Description: 数据质量预警规则
 * @Author: jeecg-boot
 * @Date:   2023-10-12
 * @Version: V1.0
 */
@Api(tags="数据质量预警规则")
@RestController
@RequestMapping("/dqc/ruleAlterInf")
@Slf4j
public class RuleAlterInfController extends JeecgController<RuleAlterInf, IRuleAlterInfService> {
	@Autowired
	private IRuleAlterInfService ruleAlterInfService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ruleAlterInf
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "数据质量预警规则-分页列表查询")
	@ApiOperation(value="数据质量预警规则-分页列表查询", notes="数据质量预警规则-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<RuleAlterInf>> queryPageList(RuleAlterInf ruleAlterInf,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) throws ParseException {
		QueryWrapper<RuleAlterInf> queryWrapper = QueryGenerator.initQueryWrapper(ruleAlterInf, req.getParameterMap());
		// 基于时间查询
		String[] createdAtArr = req.getParameterValues("createdAtArr[]");
		if (createdAtArr != null && createdAtArr.length == 2) {
			String beginCreatedAt = StringUtils.replace(createdAtArr[0], "+", "");
			String endCreatedAt = StringUtils.replace(createdAtArr[1], "+", "");
			queryWrapper.between("created_at", beginCreatedAt, endCreatedAt);
		}
		Page<RuleAlterInf> page = new Page<RuleAlterInf>(pageNo, pageSize);
		IPage<RuleAlterInf> pageList = ruleAlterInfService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ruleAlterInf
	 * @return
	 */
	@AutoLog(value = "数据质量预警规则-添加")
	@ApiOperation(value="数据质量预警规则-添加", notes="数据质量预警规则-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:rule_alter_inf:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody RuleAlterInf ruleAlterInf) {
		ruleAlterInfService.save(ruleAlterInf);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ruleAlterInf
	 * @return
	 */
	@AutoLog(value = "数据质量预警规则-编辑")
	@ApiOperation(value="数据质量预警规则-编辑", notes="数据质量预警规则-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:rule_alter_inf:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody RuleAlterInf ruleAlterInf) {
		ruleAlterInfService.updateById(ruleAlterInf);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数据质量预警规则-通过id删除")
	@ApiOperation(value="数据质量预警规则-通过id删除", notes="数据质量预警规则-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:rule_alter_inf:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		ruleAlterInfService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "数据质量预警规则-批量删除")
	@ApiOperation(value="数据质量预警规则-批量删除", notes="数据质量预警规则-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:rule_alter_inf:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ruleAlterInfService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "数据质量预警规则-通过id查询")
	@ApiOperation(value="数据质量预警规则-通过id查询", notes="数据质量预警规则-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<RuleAlterInf> queryById(@RequestParam(name="id",required=true) String id) {
		RuleAlterInf ruleAlterInf = ruleAlterInfService.getById(id);
		if(ruleAlterInf==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ruleAlterInf);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ruleAlterInf
    */
    //@RequiresPermissions("org.jeecg.modules.demo:rule_alter_inf:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RuleAlterInf ruleAlterInf) {
        return super.exportXls(request, ruleAlterInf, RuleAlterInf.class, "数据质量预警规则");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("rule_alter_inf:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, RuleAlterInf.class);
    }

}
