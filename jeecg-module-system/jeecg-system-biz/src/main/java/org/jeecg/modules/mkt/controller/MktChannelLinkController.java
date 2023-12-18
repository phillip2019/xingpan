package org.jeecg.modules.mkt.controller;

import java.util.ArrayList;
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
import org.jeecg.modules.mkt.entity.MktChannelLink;
import org.jeecg.modules.mkt.service.IMktChannelLinkService;

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
        return super.importExcel(request, response, MktChannelLink.class);
    }

}
