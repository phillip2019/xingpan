package org.jeecg.modules.et.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.et.entity.EtBuProjectEvent;
import org.jeecg.modules.et.service.IEtBuProjectEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: et_bu_project_event
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Api(tags="et_bu_project_event")
@RestController
@RequestMapping("/et/etBuProjectEvent")
@Slf4j
public class EtBuProjectEventController extends JeecgController<EtBuProjectEvent, IEtBuProjectEventService> {
	@Autowired
	private IEtBuProjectEventService etBuProjectEventService;
	
	/**
	 * 分页列表查询
	 *
	 * @param etBuProjectEvent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "et_bu_project_event-分页列表查询")
	@ApiOperation(value="et_bu_project_event-分页列表查询", notes="et_bu_project_event-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<EtBuProjectEvent>> queryPageList(EtBuProjectEvent etBuProjectEvent,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<EtBuProjectEvent> queryWrapper = QueryGenerator.initQueryWrapper(etBuProjectEvent, req.getParameterMap());
		Page<EtBuProjectEvent> page = new Page<EtBuProjectEvent>(pageNo, pageSize);
		IPage<EtBuProjectEvent> pageList = etBuProjectEventService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param etBuProjectEvent
	 * @return
	 */
	@AutoLog(value = "et_bu_project_event-添加")
	@ApiOperation(value="et_bu_project_event-添加", notes="et_bu_project_event-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:et_bu_project_event:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody EtBuProjectEvent etBuProjectEvent) {
		etBuProjectEventService.save(etBuProjectEvent);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param etBuProjectEvent
	 * @return
	 */
	@AutoLog(value = "et_bu_project_event-编辑")
	@ApiOperation(value="et_bu_project_event-编辑", notes="et_bu_project_event-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:et_bu_project_event:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody EtBuProjectEvent etBuProjectEvent) {
		etBuProjectEventService.updateById(etBuProjectEvent);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "et_bu_project_event-通过id删除")
	@ApiOperation(value="et_bu_project_event-通过id删除", notes="et_bu_project_event-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:et_bu_project_event:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		etBuProjectEventService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "et_bu_project_event-批量删除")
	@ApiOperation(value="et_bu_project_event-批量删除", notes="et_bu_project_event-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:et_bu_project_event:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.etBuProjectEventService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "et_bu_project_event-通过id查询")
	@ApiOperation(value="et_bu_project_event-通过id查询", notes="et_bu_project_event-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<EtBuProjectEvent> queryById(@RequestParam(name="id",required=true) String id) {
		EtBuProjectEvent etBuProjectEvent = etBuProjectEventService.getById(id);
		if(etBuProjectEvent==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(etBuProjectEvent);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param etBuProjectEvent
    */
    //@RequiresPermissions("org.jeecg.modules.demo:et_bu_project_event:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EtBuProjectEvent etBuProjectEvent) {
        return super.exportXls(request, etBuProjectEvent, EtBuProjectEvent.class, "et_bu_project_event");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("et_bu_project_event:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, EtBuProjectEvent.class);
    }

}
