package org.jeecg.modules.et.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.et.entity.*;
import org.jeecg.modules.et.service.IEtClientEventService;
import org.jeecg.modules.et.service.IEtEventService;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.model.SysPermissionTree;
import org.jeecg.modules.system.model.TreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 埋点事件
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Api(tags="埋点事件")
@RestController
@RequestMapping("/et/etEvent")
@Slf4j
public class EtEventController extends JeecgController<EtEvent, IEtEventService> {
	@Autowired
	private IEtEventService etEventService;

	@Autowired
	private IEtClientEventService etClientEventService;
	
	/**
	 * 分页列表查询
	 *
	 * @param etEvent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "埋点事件-分页列表查询")
	@ApiOperation(value="埋点事件-分页列表查询", notes="埋点事件-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<EtEvent>> queryPageList(EtEvent etEvent,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Map<String, String[]> requestMap = new HashMap<>(req.getParameterMap());
		String[] reqColumnArr = requestMap.get("column");
		String[] reqOrderArr = requestMap.get("order");
		requestMap.remove("column");
		requestMap.remove("order");
		String sortColumn = null;
		String sortSort = null;
		if (reqColumnArr.length > 0) {
			sortColumn = reqColumnArr[0];
			sortSort = reqOrderArr[0];
		}

		QueryWrapper<EtEvent> queryWrapper = QueryGenerator.initQueryWrapper(etEvent, requestMap);
		queryWrapper.orderByAsc("scene").orderByAsc("sorted");
		if (StringUtils.isNotBlank(sortColumn) && StringUtils.isNotBlank(sortSort)) {
			sortColumn = StrUtil.toUnderlineCase(sortColumn);
			if (StringUtils.equalsIgnoreCase("asc", sortSort)) {
				queryWrapper.orderByAsc(sortColumn);
			} else {
				queryWrapper.orderByDesc(sortColumn);
			}
		}
		Page<EtEvent> page = new Page<EtEvent>(pageNo, pageSize);
		IPage<EtEvent> pageList = etEventService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param etEvent
	 * @return
	 */
	@AutoLog(value = "埋点事件-添加")
	@ApiOperation(value="埋点事件-添加", notes="埋点事件-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:埋点事件:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody EtEvent etEvent) {
		etEventService.save(etEvent);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param etEvent
	 * @return
	 */
	@AutoLog(value = "埋点事件-编辑")
	@ApiOperation(value="埋点事件-编辑", notes="埋点事件-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:埋点事件:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody EtEvent etEvent) {
		etEventService.updateById(etEvent);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "埋点事件-通过id删除")
	@ApiOperation(value="埋点事件-通过id删除", notes="埋点事件-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:埋点事件:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		etEventService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "埋点事件-批量删除")
	@ApiOperation(value="埋点事件-批量删除", notes="埋点事件-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:埋点事件:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.etEventService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "埋点事件-通过id查询")
	@ApiOperation(value="埋点事件-通过id查询", notes="埋点事件-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<EtEvent> queryById(@RequestParam(name="id",required=true) String id) {
		EtEvent etEvent = etEventService.getById(id);
		if(etEvent==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(etEvent);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param etEvent
    */
    //@RequiresPermissions("org.jeecg.modules.demo:埋点事件:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EtEvent etEvent) {
    	log.info("开始导出埋点事件...");
		return etEventService.exportXls(request, etEvent, EtEventMaterial2.class, "埋点事件");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("埋点事件:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
//        return super.importExcel(request, response, EtEvent.class);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		log.info("开始导入埋点事件");
		return etEventService.importExcel(request, response, EtEventMaterial.class);
    }

	/**
	 * 查询角色授权
	 *
	 * @return
	 */
	@ApiOperation(value="埋点事件-查询客户端关联事件", notes="埋点事件-查询客户端关联事件")
	@RequestMapping(value = "/queryClientEvent", method = RequestMethod.GET)
	public Result<List<String>> queryClientEvent(@RequestParam(name = "clientId", required = true) String clientId) {
		Result<List<String>> result = new Result<>();
		try {
			List<EtClientEvent> list = etClientEventService.list(new QueryWrapper<EtClientEvent>().lambda().eq(EtClientEvent::getClientId, clientId));
			result.setResult(list.stream().map(etClientEvent -> String.valueOf(etClientEvent.getEventId())).collect(Collectors.toList()));
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 保存客户端事件关联
	 * @return
	 */
	@ApiOperation(value="埋点事件-保存客户端事件关联", notes="埋点事件-保存客户端事件关联")
	@RequestMapping(value = "/saveClientEvent", method = RequestMethod.POST)
	//@RequiresRoles({ "admin" })
	public Result<String> saveClientEvent(@RequestBody JSONObject json) {
		Result<String> result = new Result<>();
		try {
			String clientId = json.getString("clientId");
			String eventIds = json.getString("eventIds");
			String lastEventIds = json.getString("lastEventIds");
			this.etClientEventService.saveClientEvent(clientId, eventIds, lastEventIds);
			result.success("保存成功！");
		} catch (Exception e) {
			result.error500("保存客户端关联事件失败！");
			log.error(e.getMessage(), e);
		}
		return result;
	}
}
