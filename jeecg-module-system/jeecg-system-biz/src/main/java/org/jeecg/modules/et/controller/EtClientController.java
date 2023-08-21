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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.et.entity.EtClient;
import org.jeecg.modules.et.entity.EtEventProperty;
import org.jeecg.modules.et.entity.EtPlatformSiteCode;
import org.jeecg.modules.et.service.IEtClientService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.et.service.IEtPlatformSiteCodeService;
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
 * @Description: 平台客户端
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Api(tags="平台客户端")
@RestController
@RequestMapping("/et/etClient")
@Slf4j
public class EtClientController extends JeecgController<EtClient, IEtClientService> {
	@Autowired
	private IEtClientService etClientService;

	 @Autowired
	 private IEtPlatformSiteCodeService etPlatformSiteCodeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param etClient
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "平台客户端-分页列表查询")
	@ApiOperation(value="平台客户端-分页列表查询", notes="平台客户端-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<EtClient>> queryPageList(EtClient etClient,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<EtClient> queryWrapper = QueryGenerator.initQueryWrapper(etClient, req.getParameterMap());

		boolean siteFlag = false;
		List<EtPlatformSiteCode> platformSiteCodeList = null;
		EtPlatformSiteCode platformSiteCode = new EtPlatformSiteCode();
		if (StringUtils.isNotBlank(etClient.getPlatformSite())) {
			siteFlag = true;
			platformSiteCode.setPlatformSite(etClient.getPlatformSite());
		}

		if (StringUtils.isNotBlank(etClient.getPlatformSiteName())) {
			siteFlag = true;
			platformSiteCode.setPlatformSiteName(etClient.getPlatformSiteName());
		}

		if (StringUtils.isNotBlank(etClient.getPlatformSiteType())) {
			siteFlag = true;
			platformSiteCode.setPlatformSiteType(etClient.getPlatformSiteType());
		}

		if (siteFlag) {
			QueryWrapper<EtPlatformSiteCode> wrapper = QueryGenerator.initQueryWrapper(platformSiteCode, null);
			platformSiteCodeList = etPlatformSiteCodeService.list(wrapper);
			List<String> platformSiteCodeIdList = platformSiteCodeList.parallelStream().map(EtPlatformSiteCode::getId).collect(Collectors.toList());
			queryWrapper.in("platform_site_code_id", platformSiteCodeIdList);
		}
		Page<EtClient> page = new Page<>(pageNo, pageSize);
		IPage<EtClient> pageList = etClientService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param etClient
	 * @return
	 */
	@AutoLog(value = "平台客户端-添加")
	@ApiOperation(value="平台客户端-添加", notes="平台客户端-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:平台客户端:add")
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
	@AutoLog(value = "平台客户端-编辑")
	@ApiOperation(value="平台客户端-编辑", notes="平台客户端-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:平台客户端:edit")
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
	@AutoLog(value = "平台客户端-通过id删除")
	@ApiOperation(value="平台客户端-通过id删除", notes="平台客户端-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:平台客户端:delete")
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
	@AutoLog(value = "平台客户端-批量删除")
	@ApiOperation(value="平台客户端-批量删除", notes="平台客户端-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:平台客户端:deleteBatch")
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
	//@AutoLog(value = "平台客户端-通过id查询")
	@ApiOperation(value="平台客户端-通过id查询", notes="平台客户端-通过id查询")
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
    //@RequiresPermissions("org.jeecg.modules.demo:平台客户端:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EtClient etClient) {
        return super.exportXls(request, etClient, EtClient.class, "平台客户端");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("平台客户端:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, EtClient.class);
    }

}
