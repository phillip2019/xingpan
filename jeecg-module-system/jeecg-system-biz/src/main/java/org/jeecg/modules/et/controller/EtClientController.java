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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.et.entity.EtClient;
import org.jeecg.modules.et.service.IEtClientService;

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
 * @Description: et_client
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Api(tags="et_client")
@RestController
@RequestMapping("/org.jeecg.et/etClient")
@Slf4j
public class EtClientController extends JeecgController<EtClient, IEtClientService> {
	@Autowired
	private IEtClientService etClientService;
	
	/**
	 * 分页列表查询
	 *
	 * @param etClient
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "et_client-分页列表查询")
	@ApiOperation(value="et_client-分页列表查询", notes="et_client-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<EtClient>> queryPageList(EtClient etClient,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<EtClient> queryWrapper = QueryGenerator.initQueryWrapper(etClient, req.getParameterMap());
		Page<EtClient> page = new Page<EtClient>(pageNo, pageSize);
		IPage<EtClient> pageList = etClientService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param etClient
	 * @return
	 */
	@AutoLog(value = "et_client-添加")
	@ApiOperation(value="et_client-添加", notes="et_client-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:et_client:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody EtClient etClient) {
		etClientService.save(etClient);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param etClient
	 * @return
	 */
	@AutoLog(value = "et_client-编辑")
	@ApiOperation(value="et_client-编辑", notes="et_client-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:et_client:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody EtClient etClient) {
		etClientService.updateById(etClient);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "et_client-通过id删除")
	@ApiOperation(value="et_client-通过id删除", notes="et_client-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:et_client:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		etClientService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "et_client-批量删除")
	@ApiOperation(value="et_client-批量删除", notes="et_client-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:et_client:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.etClientService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "et_client-通过id查询")
	@ApiOperation(value="et_client-通过id查询", notes="et_client-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<EtClient> queryById(@RequestParam(name="id",required=true) String id) {
		EtClient etClient = etClientService.getById(id);
		if(etClient==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(etClient);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param etClient
    */
    //@RequiresPermissions("org.jeecg.modules.demo:et_client:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EtClient etClient) {
        return super.exportXls(request, etClient, EtClient.class, "et_client");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("et_client:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, EtClient.class);
    }

}
