package org.jeecg.modules.ibf.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ibf.entity.IbfMarketFinance;
import org.jeecg.modules.ibf.entity.IbfMarketFlow;
import org.jeecg.modules.ibf.service.IIbfMarketFlowService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

 /**
 * @Description: 业财一体-每日填报市场流量
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
@Api(tags="业财一体-每日填报市场流量")
@RestController
@RequestMapping("/ibf/ibfMarketFlow")
@Slf4j
public class IbfMarketFlowController extends JeecgController<IbfMarketFlow, IIbfMarketFlowService> {
	@Autowired
	private IIbfMarketFlowService ibfMarketFlowService;

	@Lazy
	@Autowired
	private CommonAPI commonApi;

	public static final String DICT_CODE = "short_market_id";
	
	/**
	 * 分页列表查询
	 *
	 * @param ibfMarketFlow
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "业财一体-每日填报市场流量-分页列表查询")
	@ApiOperation(value="业财一体-每日填报市场流量-分页列表查询", notes="业财一体-每日填报市场流量-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<IbfMarketFlow>> queryPageList(IbfMarketFlow ibfMarketFlow,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<IbfMarketFlow> queryWrapper = QueryGenerator.initQueryWrapper(ibfMarketFlow, req.getParameterMap());
		Page<IbfMarketFlow> page = new Page<IbfMarketFlow>(pageNo, pageSize);
		IPage<IbfMarketFlow> pageList = ibfMarketFlowService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ibfMarketFlow
	 * @return
	 */
	@AutoLog(value = "业财一体-每日填报市场流量-添加")
	@ApiOperation(value="业财一体-每日填报市场流量-添加", notes="业财一体-每日填报市场流量-添加")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody IbfMarketFlow ibfMarketFlow) {
		ibfMarketFlowService.save(ibfMarketFlow);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ibfMarketFlow
	 * @return
	 */
	@AutoLog(value = "业财一体-每日填报市场流量-编辑")
	@ApiOperation(value="业财一体-每日填报市场流量-编辑", notes="业财一体-每日填报市场流量-编辑")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody IbfMarketFlow ibfMarketFlow) {
		ibfMarketFlowService.updateById(ibfMarketFlow);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "业财一体-每日填报市场流量-通过id删除")
	@ApiOperation(value="业财一体-每日填报市场流量-通过id删除", notes="业财一体-每日填报市场流量-通过id删除")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		ibfMarketFlowService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "业财一体-每日填报市场流量-批量删除")
	@ApiOperation(value="业财一体-每日填报市场流量-批量删除", notes="业财一体-每日填报市场流量-批量删除")
	@RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ibfMarketFlowService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "业财一体-每日填报市场流量-通过id查询")
	@ApiOperation(value="业财一体-每日填报市场流量-通过id查询", notes="业财一体-每日填报市场流量-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<IbfMarketFlow> queryById(@RequestParam(name="id",required=true) String id) {
		IbfMarketFlow ibfMarketFlow = ibfMarketFlowService.getById(id);
		if(ibfMarketFlow==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ibfMarketFlow);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ibfMarketFlow
    */
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IbfMarketFlow ibfMarketFlow) {
		// 从url中获取businessVersion参数
		String businessVersion = request.getParameter("businessVersion");
		// 根据businessVersion版本参数判断，返回不同的excel模板
		String title = "市场流量(BOSS)-每日填报";
		if(businessVersion.equals("BOSS")){
			title = "市场流量(BOSS)-每日填报";
		} else if (businessVersion.equals("OPERATION")) {
			title = "市场流量(运营)-每日填报";
		}
		return super.exportXls(request, ibfMarketFlow, IbfMarketFlow.class, title);
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		// 从url中获取businessVersion参数
		String businessVersion = request.getParameter("businessVersion");
		// 只支持BOSS、OPERATION、FINANCE参数
		if(oConvertUtils.isEmpty(businessVersion)){
			return Result.error("业务版本不能为空");
		}
		if(!businessVersion.equals("BOSS") && !businessVersion.equals("OPERATION") && !businessVersion.equals("FINANCE")){
			return Result.error("业务版本参数错误");
		}
		return customImportExcel(request, response, IbfMarketFlow.class, businessVersion);
	}

	 /**
	  * 通过excel导入数据
	  *
	  * @param request
	  * @param response
	  * @return
	  */
	 private Result<?> customImportExcel(HttpServletRequest request, HttpServletResponse response, Class<IbfMarketFlow> clazz, String businessVersion) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		 List<DictModel> dictModelList = commonApi.queryEnableDictItemsByCode(DICT_CODE);
		 // 将dictModelList的value转换成数组
		 List<String> shortMarketIdList = dictModelList.stream().map(DictModel::getValue).collect(Collectors.toList());
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 // 获取上传文件对象
			 MultipartFile file = entity.getValue();
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<IbfMarketFlow> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
				 // 塞入businessVersion
				 for (IbfMarketFlow ibfMarketFlow : list) {
					 ibfMarketFlow.setBusinessVersion(businessVersion);
				 }
				 // 校验市场，市场必须为 shortMarketId
				 for (IbfMarketFlow ibfMarketFlow : list) {
					 String shortMarketId = ibfMarketFlow.getShortMarketId();
					 // 只有列表中的shortMarketId在字典中才行
					 if (!shortMarketIdList.contains(shortMarketId)) {
						 return Result.error("市场编号:【" + shortMarketId + "】不存在");
					 }
				 }

				 // 二元组唯一性校验，businessVersion，monthCol
				 for (IbfMarketFlow ibfMarketFlow : list) {
					 Date dateCol = ibfMarketFlow.getDateCol();
					 // 校验唯一性
					 List<IbfMarketFlow> ibfMarketFlowList = service.list(new QueryWrapper<IbfMarketFlow>()
							 .eq("business_version", businessVersion)
							 .eq("date_col", dateCol)
							 .last("limit 1")
					 );
					 if (!ibfMarketFlowList.isEmpty()) {
						 ibfMarketFlow.setId(ibfMarketFlowList.get(0).getId());
					 }
				 }

				 //update-begin-author:taoyan date:20190528 for:批量插入数据
				 long start = System.currentTimeMillis();
				 service.saveOrUpdateBatch(list);
				 //400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
				 //1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 //update-end-author:taoyan date:20190528 for:批量插入数据
				 return Result.ok("文件导入成功！数据行数：" + list.size());
			 } catch (Exception e) {
				 //update-begin-author:taoyan date:20211124 for: 导入数据重复增加提示
				 String msg = e.getMessage();
				 log.error(msg, e);
				 if(msg!=null && msg.contains("Duplicate entry")){
					 return Result.error("文件导入失败:有重复数据！");
				 }else{
					 return Result.error("文件导入失败:" + e.getMessage());
				 }
				 //update-end-author:taoyan date:20211124 for: 导入数据重复增加提示
			 } finally {
				 try {
					 file.getInputStream().close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
			 }
		 }
		 return Result.error("文件导入失败！");
	 }

	 @RequiresPermissions("org.jeecg.modules.demo:ibf_market_flow:add")
@ApiOperation(value = "业财一体-每日填报市场流量-唯一性校验", notes = "业财一体-每日填报市场流量-唯一性校验")
@GetMapping(value = "/checkUnique")
public Result<IbfMarketFlow> checkUnique(@RequestParam(name = "businessVersion", required = true) String businessVersion,
                                         @RequestParam(name = "shortMarketId", required = true) String shortMarketId,
                                         @RequestParam(name = "dateCol", required = true) String dateCol) {
    log.info("Received parameters - businessVersion: {}, shortMarketId: {}, dateCol: {}", businessVersion, shortMarketId, dateCol);
    IbfMarketFlow ibfMarketFlow = ibfMarketFlowService.checkUnique(businessVersion, shortMarketId, dateCol);
    if (ibfMarketFlow == null) {
        return Result.OK(null);
    }
    return Result.OK(ibfMarketFlow);
}

}
