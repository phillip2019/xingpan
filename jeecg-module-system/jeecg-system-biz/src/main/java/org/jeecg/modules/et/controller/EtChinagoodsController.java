package org.jeecg.modules.et.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.EtDynamicTable;
import org.jeecg.common.constant.enums.EtEnvEnum;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.et.entity.EtChinagoods;
import org.jeecg.modules.et.service.IEtChinagoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Arrays;

 /**
 * @Description: et_chinagoods
 * @Author: jeecg-boot
 * @Date:   2022-11-22
 * @Version: V1.0
 */
@Api(tags="et_chinagoods")
@RestController
@RequestMapping("/et/uaeChinagoods")
@Slf4j
public class EtChinagoodsController extends JeecgController<EtChinagoods, IEtChinagoodsService> {
	@Autowired
	private IEtChinagoodsService etChinagoodsService;

	/**
	 * 分页列表查询
	 *
	 * @param etChinagoods
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "et_chinagoods-分页列表查询")
	@ApiOperation(value="et_chinagoods-分页列表查询", notes="et_chinagoods-分页列表查询")
	@GetMapping(value = "/list")
	@EtDynamicTable(value = "et_chinagoods")
	public Result<IPage<EtChinagoods>> queryPageList(EtChinagoods etChinagoods,
													 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													 HttpServletRequest req) throws ParseException {
		String env = req.getParameter("env");
		if (EtEnvEnum.getEtEnvEnumByEnv(env) == null) {
			return Result.error("环境不正确，请传递正确环境");
		}

		QueryWrapper<EtChinagoods> queryWrapper = new QueryWrapper<>(etChinagoods);
		Page<EtChinagoods> page = new Page<EtChinagoods>(pageNo, pageSize);
		if (EtEnvEnum.TEST.etEnv.equals(env)) {
			// 基于时间查询
			String[] createdAtArr = req.getParameterValues("createdAtArr[]");
			if (createdAtArr != null && createdAtArr.length == 2) {
				long beginCreatedAt = DateUtils.parseTimestamp(createdAtArr[0], "yyyy-MM-dd HH:mm:ss").toInstant().getEpochSecond() * 1000;
				long endCreatedAt = DateUtils.parseTimestamp(createdAtArr[1], "yyyy-MM-dd HH:mm:ss").toInstant().getEpochSecond() * 1000;
				queryWrapper.between("created_at", beginCreatedAt, endCreatedAt);
			}
			queryWrapper.orderByDesc("created_at");
			IPage<EtChinagoods> pageList = etChinagoodsService.page(page, queryWrapper);
			return Result.OK(pageList);
		}

		IPage<EtChinagoods> pageList = etChinagoodsService.queryKafkaMessage(etChinagoods, pageNo, pageSize, req);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param etChinagoods
	 * @return
	 */
	@AutoLog(value = "et_chinagoods-添加")
	@ApiOperation(value="et_chinagoods-添加", notes="et_chinagoods-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:et_chinagoods:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody EtChinagoods etChinagoods) {
		etChinagoodsService.save(etChinagoods);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param etChinagoods
	 * @return
	 */
	@AutoLog(value = "et_chinagoods-编辑")
	@ApiOperation(value="et_chinagoods-编辑", notes="et_chinagoods-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:et_chinagoods:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody EtChinagoods etChinagoods) {
		etChinagoodsService.updateById(etChinagoods);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "et_chinagoods-通过id删除")
	@ApiOperation(value="et_chinagoods-通过id删除", notes="et_chinagoods-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:et_chinagoods:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		etChinagoodsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "et_chinagoods-批量删除")
	@ApiOperation(value="et_chinagoods-批量删除", notes="et_chinagoods-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:et_chinagoods:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.etChinagoodsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "et_chinagoods-通过id查询")
	@ApiOperation(value="et_chinagoods-通过id查询", notes="et_chinagoods-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<EtChinagoods> queryById(@RequestParam(name="id",required=true) String id) {
		EtChinagoods etChinagoods = etChinagoodsService.getById(id);
		if(etChinagoods ==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(etChinagoods);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param etChinagoods
    */
    //@RequiresPermissions("org.jeecg.modules.demo:et_chinagoods:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EtChinagoods etChinagoods) {
        return super.exportXls(request, etChinagoods, EtChinagoods.class, "et_chinagoods");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("et_chinagoods:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, EtChinagoods.class);
    }

}
