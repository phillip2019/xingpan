package org.jeecg.modules.mkt.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ibf.entity.IbfMarketFinance;
import org.jeecg.modules.mkt.entity.MktChannelLink;
import org.jeecg.modules.mkt.service.IMktChannelLinkService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.mkt.util.UrlUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

import static cn.hutool.core.bean.BeanUtil.copyProperties;

/**
 * @Description: 营销渠道链接
 * @Author: jeecg-boot
 * @Date:   2023-12-04
 * @Version: V1.0
 */
@Api(tags="营销渠道链接")
@RestController
@RequestMapping("/mkt/mktChannelLink")
@Slf4j
public class MktChannelLinkController extends JeecgController<MktChannelLink, IMktChannelLinkService> {
	@Autowired
	private IMktChannelLinkService mktChannelLinkService;

	public static final String DICT_CODE = "CHANNEL_LINK_STATUS";

	@Lazy
	@Autowired
	private CommonAPI commonApi;

	/**
	 * 分页列表查询
	 *
	 * @param mktChannelLink
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "营销渠道链接-分页列表查询")
	@ApiOperation(value="营销渠道链接-分页列表查询", notes="营销渠道链接-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MktChannelLink>> queryPageList(MktChannelLink mktChannelLink,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MktChannelLink> queryWrapper = QueryGenerator.initQueryWrapper(mktChannelLink, req.getParameterMap());
		Page<MktChannelLink> page = new Page<MktChannelLink>(pageNo, pageSize);
		IPage<MktChannelLink> pageList = mktChannelLinkService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param mktChannelLink
	 * @return
	 */
	@AutoLog(value = "营销渠道链接-添加")
	@ApiOperation(value="营销渠道链接-添加", notes="营销渠道链接-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:mkt_channel_link:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody MktChannelLink mktChannelLink) {
		// 生成带参链接
		if (StringUtils.isNotBlank(mktChannelLink.getPcSourceUrl())) {
			mktChannelLink.setPcTargetUrl(UrlUtils.genAdvUrl(mktChannelLink.getPcSourceUrl(), mktChannelLink, true));
		}
		if (StringUtils.isNotBlank(mktChannelLink.getWapSourceUrl())) {
			mktChannelLink.setWapTargetUrl(UrlUtils.genAdvUrl(mktChannelLink.getWapSourceUrl(), mktChannelLink, false));
		}
		mktChannelLinkService.save(mktChannelLink);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param mktChannelLink
	 * @return
	 */
	@AutoLog(value = "营销渠道链接-编辑")
	@ApiOperation(value="营销渠道链接-编辑", notes="营销渠道链接-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:mkt_channel_link:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody MktChannelLink mktChannelLink) {
		// 若编辑内容中包含pcSourceUrl，则生成带参数的pcTargetUrl
		if (StringUtils.isNotBlank(mktChannelLink.getPcSourceUrl())) {
			mktChannelLink.setPcTargetUrl(UrlUtils.genAdvUrl(mktChannelLink.getPcSourceUrl(), mktChannelLink, true));
		}
		if (StringUtils.isNotBlank(mktChannelLink.getWapSourceUrl())) {
			mktChannelLink.setWapTargetUrl(UrlUtils.genAdvUrl(mktChannelLink.getWapSourceUrl(), mktChannelLink, false));
		}
		mktChannelLinkService.updateById(mktChannelLink);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "营销渠道链接-通过id删除")
	@ApiOperation(value="营销渠道链接-通过id删除", notes="营销渠道链接-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:mkt_channel_link:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		mktChannelLinkService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "营销渠道链接-批量删除")
	@ApiOperation(value="营销渠道链接-批量删除", notes="营销渠道链接-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:mkt_channel_link:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.mktChannelLinkService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	 /**
	  *  批量更新状态
	  * @param mktChannelLink 投流链接
	  * @return 请求参数
	  */
	 @AutoLog(value = "营销渠道链接-批量更新")
	 @ApiOperation(value="营销渠道链接-批量更新", notes="营销渠道链接-批量更新")
	 //@RequiresPermissions("org.jeecg.modules.demo:mkt_channel_link:updateBatch")
	 @PutMapping(value = "/updateBatch")
	 public Result<String> updateBatch(@RequestBody(required=true) MktChannelLink mktChannelLink) {
		 List<MktChannelLink> mktChannelLinks = new ArrayList<>();
		 for (String id : mktChannelLink.getIds()) {
			 MktChannelLink mktChannelLink1 = new MktChannelLink();
			 copyProperties(mktChannelLink, mktChannelLink1);
			 mktChannelLink1.setId(id);
			 mktChannelLinks.add(mktChannelLink1);
		 }
		 // 批量更新带参链接
		 mktChannelLinks.forEach(mktChannelLink1 -> {
			 String pcSourceUrl = mktChannelLink1.getPcSourceUrl();
			 String wapSourceUrl = mktChannelLink1.getWapSourceUrl();
			 if (StringUtils.isNotBlank(pcSourceUrl)) {
				 mktChannelLink1.setPcTargetUrl(UrlUtils.genAdvUrl(pcSourceUrl, mktChannelLink1, true));
			 }
			 if (StringUtils.isNotBlank(wapSourceUrl)) {
				 mktChannelLink1.setWapTargetUrl(UrlUtils.genAdvUrl(wapSourceUrl, mktChannelLink1, false));
			 }
		 });
		 this.mktChannelLinkService.updateBatchById(mktChannelLinks);
		 return Result.OK("批量更新成功!");
	 }

	/**
	 *  批量更新
	 * @param mktChannelLinkList
	 * @return
	 */
	@AutoLog(value = "营销渠道链接-批量更新")
	@ApiOperation(value="营销渠道链接-批量更新", notes="营销渠道链接-批量更新")
	//@RequiresPermissions("org.jeecg.modules.demo:mkt_channel_link:updateBatch")
	@PutMapping(value = "/updateBatch2")
	public Result<String> updateBatch(@RequestBody(required=true) List<MktChannelLink> mktChannelLinkList) {
		if (!mktChannelLinkList.isEmpty()) {
			// 批量更新带参链接
			mktChannelLinkList.forEach(mktChannelLink -> {
				String pcSourceUrl = mktChannelLink.getPcSourceUrl();
				String wapSourceUrl = mktChannelLink.getWapSourceUrl();
				if (StringUtils.isNotBlank(pcSourceUrl)) {
					mktChannelLink.setPcTargetUrl(UrlUtils.genAdvUrl(pcSourceUrl, mktChannelLink, true));
				}
				if (StringUtils.isNotBlank(wapSourceUrl)) {
					mktChannelLink.setWapTargetUrl(UrlUtils.genAdvUrl(wapSourceUrl, mktChannelLink, false));
				}
			});
			this.mktChannelLinkService.updateBatchById(mktChannelLinkList);
		}
		return Result.OK("批量更新成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "营销渠道链接-通过id查询")
	@ApiOperation(value="营销渠道链接-通过id查询", notes="营销渠道链接-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MktChannelLink> queryById(@RequestParam(name="id",required=true) String id) {
		MktChannelLink mktChannelLink = mktChannelLinkService.getById(id);
		if(mktChannelLink==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(mktChannelLink);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param mktChannelLink
    */
    //@RequiresPermissions("org.jeecg.modules.demo:mkt_channel_link:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MktChannelLink mktChannelLink) {
        return super.exportXls(request, mktChannelLink, MktChannelLink.class, "营销渠道链接");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("mkt_channel_link:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		return customImportExcel(request, response, MktChannelLink.class);
    }

	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	private Result<?> customImportExcel(HttpServletRequest request, HttpServletResponse response, Class<MktChannelLink> clazz) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		Date now = new Date();
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
				List<MktChannelLink> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
				for (MktChannelLink mktChannelLink : list) {
					// 校验状态
					Integer status = mktChannelLink.getStatus();
					if (Objects.isNull(status)) {
						mktChannelLink.setStatus(1);
					}
					// 生成带参数链接
					String pcSourceUrl = mktChannelLink.getPcSourceUrl();
					String wapSourceUrl = mktChannelLink.getWapSourceUrl();

					// 导入设置编码
					mktChannelLink.setPcTargetUrl(UrlUtils.genAdvUrl(pcSourceUrl, mktChannelLink, true));
					mktChannelLink.setWapTargetUrl(UrlUtils.genAdvUrl(wapSourceUrl, mktChannelLink, false));
				}
				//update-begin-author:taoyan date:20190528 for:批量插入数据
				long start = System.currentTimeMillis();
				service.saveOrUpdateBatch(list);
				//400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
				//1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
				log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				//update-end-author:taoyan date:20190528 for:批量插入数据
				return Result.ok("文件导入成功！数据行数：" + list.size());
			} catch (InvocationTargetException e) {
				log.error("文件导入失败: ", e);
				return Result.error("文件导入失败:" + e.getTargetException().getMessage());
			} catch (Exception e) {
				//update-begin-author:taoyan date:20211124 for: 导入数据重复增加提示
				String msg = e.getMessage();
				log.error(msg, e);
				if (msg != null && msg.contains("Duplicate entry")) {
					return Result.error("文件导入失败:有重复数据！");
				} else {
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
}
