package org.jeecg.modules.et.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.et.entity.EtEvent;
import org.jeecg.modules.et.entity.EtEventMaterial;
import org.jeecg.modules.et.service.IEtEventService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.ma.entity.MaActiveTaiKaMaterial;
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
		QueryWrapper<EtEvent> queryWrapper = QueryGenerator.initQueryWrapper(etEvent, req.getParameterMap());
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
		return etEventService.exportXls(request, etEvent, EtEventMaterial.class, "埋点事件");
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

}
