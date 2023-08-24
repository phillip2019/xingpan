package org.jeecg.modules.et.controller;

import java.text.ParseException;
import java.time.LocalDate;
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
import org.jeecg.modules.et.entity.EtEventLocal;
import org.jeecg.modules.et.service.IEtEventLocalService;

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
 * @Description: CK中实时埋点事件
 * @Author: jeecg-boot
 * @Date:   2023-08-24
 * @Version: V1.0
 */
@Api(tags="CK中实时埋点事件")
@RestController
@RequestMapping("/et/etEventLocal")
@Slf4j
public class EtEventLocalController extends JeecgController<EtEventLocal, IEtEventLocalService> {
	@Autowired
	private IEtEventLocalService etEventLocalService;
	
	/**
	 * 分页列表查询
	 *
	 * @param etEventLocal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "CK中实时埋点事件-分页列表查询")
	@ApiOperation(value="CK中实时埋点事件-分页列表查询", notes="CK中实时埋点事件-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<EtEventLocal>> queryPageList(EtEventLocal etEventLocal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) throws ParseException {
		QueryWrapper<EtEventLocal> queryWrapper = QueryGenerator.initQueryWrapper(etEventLocal, req.getParameterMap());
		Date now = new Date();
		String minDs = DateUtils.formatDate(now, "yyyyMMdd");
		String maxDs = DateUtils.formatDate(now, "yyyyMMdd");
		// 基于时间查询
		String[] createdAtArr = req.getParameterValues("createdAtArr[]");
		if (createdAtArr != null && createdAtArr.length == 2) {
			long beginCreatedAt = DateUtils.parseTimestamp(createdAtArr[0], "yyyy-MM-dd HH:mm:ss").toInstant().getEpochSecond() * 1000;
			long endCreatedAt = DateUtils.parseTimestamp(createdAtArr[1], "yyyy-MM-dd HH:mm:ss").toInstant().getEpochSecond() * 1000;
			minDs = StringUtils.replace(createdAtArr[0], "-", "");
			maxDs = StringUtils.replace(createdAtArr[1], "-", "");
			queryWrapper.between("time", beginCreatedAt, endCreatedAt);
		}
		// 默认查询当天数据
		queryWrapper.between("ds", minDs, maxDs);
		queryWrapper.orderByDesc("time");
		Page<EtEventLocal> page = new Page<EtEventLocal>(pageNo, pageSize);
		IPage<EtEventLocal> pageList = etEventLocalService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param etEventLocal
	 * @return
	 */
	@AutoLog(value = "CK中实时埋点事件-添加")
	@ApiOperation(value="CK中实时埋点事件-添加", notes="CK中实时埋点事件-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:s_et_event_tracking_di_local:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody EtEventLocal etEventLocal) {
		etEventLocalService.save(etEventLocal);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param etEventLocal
	 * @return
	 */
	@AutoLog(value = "CK中实时埋点事件-编辑")
	@ApiOperation(value="CK中实时埋点事件-编辑", notes="CK中实时埋点事件-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:s_et_event_tracking_di_local:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody EtEventLocal etEventLocal) {
		etEventLocalService.updateById(etEventLocal);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "CK中实时埋点事件-通过id删除")
	@ApiOperation(value="CK中实时埋点事件-通过id删除", notes="CK中实时埋点事件-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:s_et_event_tracking_di_local:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		etEventLocalService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "CK中实时埋点事件-批量删除")
	@ApiOperation(value="CK中实时埋点事件-批量删除", notes="CK中实时埋点事件-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:s_et_event_tracking_di_local:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.etEventLocalService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "CK中实时埋点事件-通过id查询")
	@ApiOperation(value="CK中实时埋点事件-通过id查询", notes="CK中实时埋点事件-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<EtEventLocal> queryById(@RequestParam(name="id",required=true) String id) {
		EtEventLocal etEventLocal = etEventLocalService.getById(id);
		if(etEventLocal==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(etEventLocal);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param etEventLocal
    */
    //@RequiresPermissions("org.jeecg.modules.demo:s_et_event_tracking_di_local:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EtEventLocal etEventLocal) {
        return super.exportXls(request, etEventLocal, EtEventLocal.class, "CK中实时埋点事件");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("s_et_event_tracking_di_local:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, EtEventLocal.class);
    }

}
