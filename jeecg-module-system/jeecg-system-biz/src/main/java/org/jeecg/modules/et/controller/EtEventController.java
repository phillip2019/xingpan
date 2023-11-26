package org.jeecg.modules.et.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.et.entity.*;
import org.jeecg.modules.et.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
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

	public static final String COMMA_DELIMITER = ",";

	@Autowired
	private IEtEventService etEventService;

	@Autowired
	private IEtClientEventService etClientEventService;

	@Autowired
	private IEtClientService etClientService;

	@Autowired
	private IEtBuProjectEventService etBuProjectEventService;

	@Autowired
	private IEtClientEventScreenshotService etClientEventScreenshotService;

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
		if (StringUtils.isNotBlank(etEvent.getBuProjectNameId())) {
			String buProjectNameId = etEvent.getBuProjectNameId();
			EtBuProjectEvent etBuProjectEvent = new EtBuProjectEvent();
			etBuProjectEvent.setBuProjectId(buProjectNameId);
			QueryWrapper<EtBuProjectEvent>  etBuProjectEventQueryWrapper = QueryGenerator.initQueryWrapper(etBuProjectEvent, null);
			List<EtBuProjectEvent> buProjectEventList = etBuProjectEventService.list(etBuProjectEventQueryWrapper);
			List<String> eventIds = buProjectEventList.stream().map(EtBuProjectEvent::getEventId).collect(Collectors.toList());
			if (!eventIds.isEmpty()) {
				queryWrapper.in("id", eventIds);
			}
		}
		if (StringUtils.isNotBlank(etEvent.getClientNames())) {
			String clientName = etEvent.getClientNames();
			List<String> eventIds = etClientEventService.listEventIdByClientName(clientName);
			if (!eventIds.isEmpty()) {
				queryWrapper.in("id", eventIds);
			}
		}
		queryWrapper.orderByAsc("scene").orderByAsc("sorted");
		if (StringUtils.isNotBlank(sortColumn) && StringUtils.isNotBlank(sortSort)) {
			sortColumn = StrUtil.toUnderlineCase(sortColumn);
			if (StringUtils.equalsIgnoreCase("asc", sortSort)) {
				queryWrapper.orderByAsc(sortColumn);
			} else {
				queryWrapper.orderByDesc(sortColumn);
			}
		}
		// 支持事件英文名称精准匹配
		if (StringUtils.isNotBlank(etEvent.getName2())) {
			queryWrapper.eq("name", etEvent.getName2());
		}
		Page<EtEvent> page = new Page<EtEvent>(pageNo, pageSize);
		IPage<EtEvent> pageList = etEventService.page(page, queryWrapper);
		// 查询所有客户端，记录客户端ID, Name map
		List<EtClient> etClientList = etClientService.list();
		Map<String, String> clientId2ClientNameMap = new HashMap<>();
		for (EtClient ec : etClientList) {
			clientId2ClientNameMap.put(ec.getId(), ec.getName());
		}

		// 查询客户端属性，将客户端名称填充在clientNames中
		List<EtEvent> etEventList = pageList.getRecords();
		List<String> eventIds = etEventList.stream().map(EtEvent::getId).collect(Collectors.toList());

		EtClientEvent etClientEvent = new EtClientEvent();
		QueryWrapper<EtClientEvent>  etClientEventQueryWrapper = QueryGenerator.initQueryWrapper(etClientEvent, null);
		if (!eventIds.isEmpty()) {
			etClientEventQueryWrapper.in("event_id", eventIds);
			log.info("客户端事件编号为: {}", eventIds);
		}
		List<EtClientEvent> etClientEventList = etClientEventService.list(etClientEventQueryWrapper);
		List<String> clientEventIds = etClientEventList.stream().map(EtClientEvent::getId).collect(Collectors.toList());
		Map<String, List<EtClientEventScreenshot>> groupClientEventIdMap = new HashMap<>(5);
		Map<String, Set<String>> eventClientNameListMap = new HashMap<>(5);
		Map<String, List<EtClientEventScreenshot>> eventScreenshotListMap = new HashMap<>(5);
		if (!clientEventIds.isEmpty()) {
			List<EtClientEventScreenshot> etClientEventScreenshotList = etClientEventScreenshotService.list(new LambdaQueryWrapper<EtClientEventScreenshot>()
					.in(EtClientEventScreenshot::getClientEventId, clientEventIds)
					.eq(EtClientEventScreenshot::getStatus, 1));
			for (EtClientEventScreenshot eces : etClientEventScreenshotList) {
				// 将etClientEventScreenshotList按照clientEventId分组，相同clientEventId的分为同一组
				groupClientEventIdMap.putIfAbsent(eces.getClientEventId(), new ArrayList<>());
				groupClientEventIdMap.get(eces.getClientEventId()).add(eces);
			}
		}
		for (EtClientEvent ce : etClientEventList) {
			String eventId = ce.getEventId();
			String clientId = ce.getClientId();
			if (!eventClientNameListMap.containsKey(eventId)) {
				eventClientNameListMap.put(eventId, new HashSet<>());
			}
			Set<String> clientNameSet = eventClientNameListMap.get(eventId);
			if (clientId2ClientNameMap.containsKey(clientId)) {
				clientNameSet.add(clientId2ClientNameMap.get(clientId));
			} else {
				log.error("不存在该EventId:{},  ClientID: {}", eventId, clientId);
			}

			eventScreenshotListMap.putIfAbsent(eventId, new ArrayList<>());
			eventScreenshotListMap.get(eventId).addAll(groupClientEventIdMap.getOrDefault(ce.getId(), new ArrayList<>()));
		}

		for (EtEvent e : etEventList) {
			String eventId = e.getId();
			Set<String> clientNameSet = eventClientNameListMap.get(eventId);
			e.setClientNames(StringUtils.join(clientNameSet, ","));
			e.setScreenshots(eventScreenshotListMap.get(eventId));
		}
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
	 * 复制
	 * @param etEvent
	 * @return
	 */
	@AutoLog(value = "埋点事件-复制")
	@ApiOperation(value="埋点事件-复制", notes="埋点事件-复制")
	//@RequiresPermissions("org.jeecg.modules.demo:埋点事件:add")
	@PostMapping(value = "/copy")
	public Result<String> copy(@RequestBody EtEvent etEvent) {
		boolean ret = etEventService.copy(etEvent);
		if (!ret) {
			return Result.error("复制失败，事件名称或事件中文名称已存在，请检查再添加！");
		}
		return Result.OK("复制成功！");
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
	 *  批量更新
	 * @param ids
	 * @param et
	 * @return
	 */
	@AutoLog(value = "埋点事件-批量更新")
	@ApiOperation(value="埋点事件-批量更新", notes="埋点事件-批量更新")
	//@RequiresPermissions("org.jeecg.modules.demo:埋点事件:updateBatch")
	@PutMapping(value = "/updateBatch")
	public Result<String> updateBatch(@RequestParam(name="ids",required=true) String ids, EtEvent et) {
		List<EtEvent> etEventList = new ArrayList<>();
		EtEvent event = null;
		for (String id : ids.split(COMMA_DELIMITER)) {
			event = new EtEvent();
			if (Objects.nonNull(et)) {
				BeanUtils.copyProperties(et, event);
			}
			event.setId(id);
			etEventList.add(event);
		}
		this.etEventService.updateBatchById(etEventList);
		return Result.OK("批量更新成功!");
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
	 * 查询客户端关联事件
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

	/**
	 * 查询业务产品关联事件
	 *
	 * @return
	 */
	@ApiOperation(value="埋点事件-查询业务产品关联事件", notes="埋点事件-查询业务产品关联事件")
	@RequestMapping(value = "/queryBuProjectEvent", method = RequestMethod.GET)
	public Result<List<String>> queryBuProjectEvent(@RequestParam(name = "buProjectId", required = true) String buProjectId) {
		Result<List<String>> result = new Result<>();
		try {
			List<EtBuProjectEvent> list = etBuProjectEventService.list(new QueryWrapper<EtBuProjectEvent>().lambda().eq(EtBuProjectEvent::getBuProjectId, buProjectId));
			result.setResult(list.stream().map(etBuProjectEvent -> String.valueOf(etBuProjectEvent.getEventId())).collect(Collectors.toList()));
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 保存业务产品事件关联
	 * @return
	 */
	@ApiOperation(value="埋点事件-保存业务产品事件关联", notes="埋点事件-保存业务产品事件关联")
	@RequestMapping(value = "/saveBuProjectEvent", method = RequestMethod.POST)
	//@RequiresRoles({ "admin" })
	public Result<String> saveBuProjectEvent(@RequestBody JSONObject json) {
		Result<String> result = new Result<>();
		try {
			String clientId = json.getString("buProjectId");
			String eventIds = json.getString("eventIds");
			String lastEventIds = json.getString("lastEventIds");
			this.etBuProjectEventService.saveBuProjectEvent(clientId, eventIds, lastEventIds);
			result.success("保存成功！");
		} catch (Exception e) {
			result.error500("保存业务产品关联事件失败！");
			log.error(e.getMessage(), e);
		}
		return result;
	}
}
