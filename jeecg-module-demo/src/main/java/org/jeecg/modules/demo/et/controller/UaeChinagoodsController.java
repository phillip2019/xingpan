package org.jeecg.modules.demo.et.controller;

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
import org.jeecg.modules.demo.et.entity.UaeChinagoods;
import org.jeecg.modules.demo.et.service.IUaeChinagoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Arrays;

 /**
 * @Description: uae_chinagoods
 * @Author: jeecg-boot
 * @Date:   2022-11-22
 * @Version: V1.0
 */
@Api(tags="uae_chinagoods")
@RestController
@RequestMapping("/et/uaeChinagoods")
@Slf4j
public class UaeChinagoodsController extends JeecgController<UaeChinagoods, IUaeChinagoodsService> {
	@Autowired
	private IUaeChinagoodsService uaeChinagoodsService;

	/**
	 * 分页列表查询
	 *
	 * @param uaeChinagoods
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "uae_chinagoods-分页列表查询")
	@ApiOperation(value="uae_chinagoods-分页列表查询", notes="uae_chinagoods-分页列表查询")
	@EtDynamicTable(value = "chinagoods")
	@GetMapping(value = "/list")
	public Result<IPage<UaeChinagoods>> queryPageList(UaeChinagoods uaeChinagoods,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) throws ParseException {
		String env = req.getParameter("env");
		if (EtEnvEnum.getEtEnvEnumByEnv(env) == null) {
			return Result.error("环境不正确，请传递正确环境");
		}

		IPage<UaeChinagoods> pageList;
		if (EtEnvEnum.TEST.etEnv.equals(env)) {
			// kafka streaming消费kafka消息
			QueryWrapper<UaeChinagoods> queryWrapper = new QueryWrapper<>(uaeChinagoods);
			// 基于时间查询
			String[] createdAtArr = req.getParameterValues("createdAtArr[]");
			if (createdAtArr != null && createdAtArr.length == 2) {
				long beginCreatedAt = DateUtils.parseTimestamp(createdAtArr[0], "yyyy-MM-dd HH:mm:ss").toInstant().getEpochSecond() * 1000;
				long endCreatedAt = DateUtils.parseTimestamp(createdAtArr[1], "yyyy-MM-dd HH:mm:ss").toInstant().getEpochSecond() * 1000;
				queryWrapper.between("created_at", beginCreatedAt, endCreatedAt);
			}
			queryWrapper.orderByDesc("created_at");

			Page<UaeChinagoods> page = new Page<UaeChinagoods>(pageNo, pageSize);
			pageList = uaeChinagoodsService.page(page, queryWrapper);
		} else {
			pageList = uaeChinagoodsService.queryKafkaMessage(uaeChinagoods, pageNo, pageSize, req);
		}
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param uaeChinagoods
	 * @return
	 */
	@AutoLog(value = "uae_chinagoods-添加")
	@ApiOperation(value="uae_chinagoods-添加", notes="uae_chinagoods-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:uae_chinagoods:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody UaeChinagoods uaeChinagoods) {
		uaeChinagoodsService.save(uaeChinagoods);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param uaeChinagoods
	 * @return
	 */
	@AutoLog(value = "uae_chinagoods-编辑")
	@ApiOperation(value="uae_chinagoods-编辑", notes="uae_chinagoods-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:uae_chinagoods:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody UaeChinagoods uaeChinagoods) {
		uaeChinagoodsService.updateById(uaeChinagoods);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "uae_chinagoods-通过id删除")
	@ApiOperation(value="uae_chinagoods-通过id删除", notes="uae_chinagoods-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:uae_chinagoods:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		uaeChinagoodsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "uae_chinagoods-批量删除")
	@ApiOperation(value="uae_chinagoods-批量删除", notes="uae_chinagoods-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:uae_chinagoods:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.uaeChinagoodsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "uae_chinagoods-通过id查询")
	@ApiOperation(value="uae_chinagoods-通过id查询", notes="uae_chinagoods-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<UaeChinagoods> queryById(@RequestParam(name="id",required=true) String id) {
		UaeChinagoods uaeChinagoods = uaeChinagoodsService.getById(id);
		if(uaeChinagoods==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(uaeChinagoods);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param uaeChinagoods
    */
    //@RequiresPermissions("org.jeecg.modules.demo:uae_chinagoods:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, UaeChinagoods uaeChinagoods) {
        return super.exportXls(request, uaeChinagoods, UaeChinagoods.class, "uae_chinagoods");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("uae_chinagoods:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, UaeChinagoods.class);
    }

}
