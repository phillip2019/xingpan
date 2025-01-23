package org.jeecg.modules.ibf.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.JacksonBuilder;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ibf.entity.IbfReportingSummary;
import org.jeecg.modules.ibf.service.IIbfReportingSummaryService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.ibf.util.IbfDateUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 填报发布汇总
 * @Author: jeecg-boot
 * @Date:   2025-01-17
 * @Version: V1.0
 */
@Api(tags="填报发布汇总")
@RestController
@RequestMapping("/ibf/ibfReportingSummary")
@Slf4j
public class IbfReportingSummaryController extends JeecgController<IbfReportingSummary, IIbfReportingSummaryService> {
	@Autowired
	private IIbfReportingSummaryService ibfReportingSummaryService;

	 @Value("${cg.marketBooth.apiUrl}")
	 private String marketBoothApiUrl = "https://testboothuser.ywcbz.com/booth-user";

	 @Value("${cg.marketBooth.clientId}")
	private String marketBoothClientID = "ArOamgo9buJVNuKeUYUqcx6CrwJb7CONy9ZOFkUXlrosk8nyTpyp38u0n1_Bvqt3XjEsD2nxwCpfc0neCGAM3LxSk9q.hABQfykGh2wRjlxCruwdu9CuTjluRA_nvbN22vqiopOPV7uBbwmUOZBL1PCZEzN5Pdnb5SRjRLhJSxs=";

	 @Value("${cg.marketBooth.ibfResourceUrl}")
	 private String ibfResourceUrl;

	 @Value("${cg.marketBooth.ibfFinanceUrl}")
	 private String ibfFinanceUrl;

	 public static final String getAuthCodeUrl = "/login/oauth/authorize";
	 /**
	 * 分页列表查询
	 *
	 * @param ibfReportingSummary
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "填报发布汇总-分页列表查询")
	@ApiOperation(value="填报发布汇总-分页列表查询", notes="填报发布汇总-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<IbfReportingSummary>> queryPageList(IbfReportingSummary ibfReportingSummary,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<IbfReportingSummary> queryWrapper = QueryGenerator.initQueryWrapper(ibfReportingSummary, req.getParameterMap());
		Page<IbfReportingSummary> page = new Page<IbfReportingSummary>(pageNo, pageSize);
		queryWrapper.eq("is_deleted", 0);
		queryWrapper.orderByDesc("month_col");
		IPage<IbfReportingSummary> pageList = ibfReportingSummaryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ibfReportingSummary
	 * @return
	 */
	@AutoLog(value = "填报发布汇总-添加")
	@ApiOperation(value="填报发布汇总-添加", notes="填报发布汇总-添加")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_reporting_summary:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody IbfReportingSummary ibfReportingSummary) {
		ibfReportingSummaryService.save(ibfReportingSummary);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ibfReportingSummary
	 * @return
	 */
	@AutoLog(value = "填报发布汇总-编辑")
	@ApiOperation(value="填报发布汇总-编辑", notes="填报发布汇总-编辑")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_reporting_summary:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody IbfReportingSummary ibfReportingSummary) {
		ibfReportingSummaryService.updateById(ibfReportingSummary);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "填报发布汇总-通过id删除")
	@ApiOperation(value="填报发布汇总-通过id删除", notes="填报发布汇总-通过id删除")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_reporting_summary:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		return this.deleteBatch(id);
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "填报发布汇总-批量删除")
	@ApiOperation(value="填报发布汇总-批量删除", notes="填报发布汇总-批量删除")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_reporting_summary:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		// 校验ID，若ID中包含发布状态的，则不允许删除
		List<IbfReportingSummary> ibfReportingSummaryList = ibfReportingSummaryService.listByIds(idList);
		List<String> publishIdList = ibfReportingSummaryList.stream()
				.filter(ibfReportingSummary -> ibfReportingSummary.getIsPublish() == 1)
				.map(IbfReportingSummary::getId)
				.collect(Collectors.toList());
		if (!publishIdList.isEmpty()) {
			return Result.error("删除失败，存在发布状态的记录，发布状态不允许删除!");
		}

		// 校验ID，已下线状态的，不允许删除
		List<IbfReportingSummary> offlineIbfReportingSummaryList = ibfReportingSummaryList.stream()
				.filter(ibfReportingSummary -> ibfReportingSummary.getIsPublish() == 2)
				.collect(Collectors.toList());
		if (!offlineIbfReportingSummaryList.isEmpty()) {
			return Result.error("删除失败，存在已下线状态的记录，已下线状态不允许删除!");
		}

		// 校验记录，若校准状态的且创建人为system(系统数据)，则不允许删除
		List<IbfReportingSummary> ibfReportingSummarySystemList = ibfReportingSummaryList.stream()
				.filter(ibfReportingSummary -> ibfReportingSummary.getIsPublish() == 0 && "system".equals(ibfReportingSummary.getCreateBy()))
				.collect(Collectors.toList());
		if (!ibfReportingSummarySystemList.isEmpty()) {
			return Result.error("删除失败，存在系统填报数据的记录，系统创建的数据不允许删除!");
		}

		this.ibfReportingSummaryService.removeBatch(ibfReportingSummaryList);
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "填报发布汇总-通过id查询")
	@ApiOperation(value="填报发布汇总-通过id查询", notes="填报发布汇总-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<IbfReportingSummary> queryById(@RequestParam(name="id",required=true) String id) {
		IbfReportingSummary ibfReportingSummary = ibfReportingSummaryService.getById(id);
		if(ibfReportingSummary==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ibfReportingSummary);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ibfReportingSummary
    */
    @RequiresPermissions("org.jeecg.modules.demo:ibf_reporting_summary:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IbfReportingSummary ibfReportingSummary) {
        return super.exportXls(request, ibfReportingSummary, IbfReportingSummary.class, "填报发布汇总");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("org.jeecg.modules.demo:ibf_reporting_summary:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, IbfReportingSummary.class);
    }

	// 添加获取当前所属月份的接口
	 @GetMapping(value = "/getCurrentMonth")
	 public Result<String> getCurrentMonth() {
		 String currentMonth = IbfDateUtil.getCurrentMonth();
		 return Result.OK(currentMonth);
	 }


	 /**
	  *   通过id复制填报记录
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "填报发布汇总-通过id复制填报记录")
	 @ApiOperation(value="填报发布汇总-通过id复制填报记录", notes="填报发布汇总-通过id复制填报记录")
	 @RequiresPermissions("org.jeecg.modules.demo:ibf_reporting_summary:copy")
	 @PostMapping(value = "/copy")
	 public Result<String> copy(@RequestParam(name="id",required=true) String id) {
//		 ibfReportingSummaryService.removeById(id);
		 IbfReportingSummary ibfReportingSummary = ibfReportingSummaryService.getById(id);
		 if( ibfReportingSummary==null ) {
			 return Result.error("复制失败，未找到对应数据");
		 }
		 // 若当前状态为发布状态且拷贝状态为未拷贝，flag=0，isDeleted=0，则可继续，否则不允许复制
		 if(!(ibfReportingSummary.getIsPublish() == 1 &&
				 ibfReportingSummary.getIsCopy() == 0 &&
				 ibfReportingSummary.getIsVisible() == 1 &&
				 ibfReportingSummary.getIsDeleted() == 0 )) {
			 return Result.error("复制失败，只有当月发布状态且未拷贝的版本可复制!");
		 }
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 // 复制当前发布版本，发布数据
		 service.copy(ibfReportingSummary, loginUser);

		 return Result.OK("复制成功!");
	 }

     /**
      *   通过id发布大屏
      *
      * @param id
      * @return
      */
     @AutoLog(value = "填报发布汇总-通过id发布大屏")
     @ApiOperation(value="填报发布汇总-通过id发布大屏", notes="填报发布汇总-通过id发布大屏")
     @RequiresPermissions("org.jeecg.modules.demo:ibf_reporting_summary:publish")
     @PostMapping(value = "/publish")
     public Result<String> publish(@RequestParam(name="id",required=true) String id) {
         IbfReportingSummary ibfReportingSummary = ibfReportingSummaryService.getById(id);
         if( ibfReportingSummary==null ) {
             return Result.error("发布失败，未找到对应数据");
         }
         // 若当前状态为核验状态且flag为1或0，则可继续，否则不允许发布
         if(!(ibfReportingSummary.getIsPublish() == 0 && (ibfReportingSummary.getFlag() == 1 || ibfReportingSummary.getFlag() == 0))) {
             return Result.error("发布失败，只有核验状态且当前大屏月份可发布!");
         }

         LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
         // 发布当前版本，发布数据
         service.publish(ibfReportingSummary, loginUser);

         return Result.OK("发布成功!");
     }

	 /**
	  * 获取商位管理系统授权码，此为1次性授权码，只有2H有效期
	  **/
	 @GetMapping(value = "/resourceUrl")
	 public Result<String> getResourceUrl() throws IOException {
		 String code = getBoothMarketAuthCode();
		 String resourceUrl = String.format("%s?code=%s", ibfResourceUrl, code);
		 return Result.OK("", resourceUrl);
	 }
	 /**
	  * 获取商位管理系统授权码，此为1次性授权码，只有2H有效期
	  **/
	 @GetMapping(value = "/financeUrl")
	 public Result<String> getFinanceUrl() throws IOException {
		 String code = getBoothMarketAuthCode();
		 String resourceUrl = String.format("%s?code=%s", ibfFinanceUrl, code);
		 return Result.OK("", resourceUrl);
	 }


	 public String getBoothMarketAuthCode() throws IOException {
		 OkHttpClient client = new OkHttpClient().newBuilder()
				 .build();
		 MediaType mediaType = MediaType.parse("application/json");
		 // 使用 ObjectMapper 构建 JSON 对象
		 ObjectMapper objectMapper = new ObjectMapper();
		 ObjectNode jsonNode = objectMapper.createObjectNode();
		 jsonNode.put("param", marketBoothClientID);
		 String json;
		 try {
			 json = objectMapper.writeValueAsString(jsonNode);
		 } catch (IOException e) {
			 log.error("Failed to convert JSON object to string", e);
			 return null;
		 }
		 log.error("请求参数地址为: {}{}, \n请求参数为: {}", marketBoothApiUrl, getAuthCodeUrl, json);
		 okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, json);
		 Request request = new Request.Builder()
				 .url(String.format("%s%s", marketBoothApiUrl, getAuthCodeUrl))
				 .method("POST", body)
				 .addHeader("Content-Type", "application/json")
				 .build();
		 okhttp3.Response response = client.newCall(request).execute();
		 // 解析返回的JSON数据，获取里面的data字段
		 if (response.isSuccessful()) {
             assert response.body() != null;
             String responseBody = response.body().string();
			 // 使用 Jackson 解析响应体
			 jsonNode = (ObjectNode) objectMapper.readTree(responseBody);
             return jsonNode.get("data").asText();
		 }
		 log.error("Failed to get Booth Market Auth Code: {}", response.body().string());
		 return null;
	 }
 }
