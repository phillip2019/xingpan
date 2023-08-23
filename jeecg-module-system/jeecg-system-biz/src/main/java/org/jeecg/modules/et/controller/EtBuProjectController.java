package org.jeecg.modules.et.controller;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.et.entity.EtBuProject;
import org.jeecg.modules.et.entity.EtEvent;
import org.jeecg.modules.et.entity.EtEventMaterial2;
import org.jeecg.modules.et.service.IEtBuProjectService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.et.service.IEtEventService;
import org.jeecg.modules.system.model.TreeModel;
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
 * @Description: 业务项目
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Api(tags="业务项目")
@RestController
@RequestMapping("/et/etBuProject")
@Slf4j
public class EtBuProjectController extends JeecgController<EtBuProject, IEtBuProjectService> {
	@Autowired
	private IEtBuProjectService etBuProjectService;

	 @Autowired
	 private IEtEventService etEventService;


	 /**
	 * 分页列表查询
	 *
	 * @param etBuProject
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "业务项目-分页列表查询")
	@ApiOperation(value="业务项目-分页列表查询", notes="业务项目-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<EtBuProject>> queryPageList(EtBuProject etBuProject,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) throws ParseException {
		QueryWrapper<EtBuProject> queryWrapper = QueryGenerator.initQueryWrapper(etBuProject, req.getParameterMap());
		Page<EtBuProject> page = new Page<EtBuProject>(pageNo, pageSize);
		// 基于时间查询
		String[] createdAtArr = req.getParameterValues("onlineTimeArr[]");
		if (createdAtArr != null && createdAtArr.length == 2) {
			Date beginCreateTime = DateUtils.parseTimestamp(createdAtArr[0], "yyyy-MM-dd");
			Date endCreateTime = DateUtils.parseTimestamp(createdAtArr[1], "yyyy-MM-dd");
			queryWrapper.between("create_time", beginCreateTime, endCreateTime);
		}
		IPage<EtBuProject> pageList = etBuProjectService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param etBuProject
	 * @return
	 */
	@AutoLog(value = "业务项目-添加")
	@ApiOperation(value="业务项目-添加", notes="业务项目-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:业务项目:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody EtBuProject etBuProject) {
		etBuProjectService.save(etBuProject);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param etBuProject
	 * @return
	 */
	@AutoLog(value = "业务项目-编辑")
	@ApiOperation(value="业务项目-编辑", notes="业务项目-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:业务项目:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody EtBuProject etBuProject) {
		etBuProjectService.updateById(etBuProject);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "业务项目-通过id删除")
	@ApiOperation(value="业务项目-通过id删除", notes="业务项目-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:业务项目:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		etBuProjectService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "业务项目-批量删除")
	@ApiOperation(value="业务项目-批量删除", notes="业务项目-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:业务项目:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.etBuProjectService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "业务项目-通过id查询")
	@ApiOperation(value="业务项目-通过id查询", notes="业务项目-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<EtBuProject> queryById(@RequestParam(name="id",required=true) String id) {
		EtBuProject etBuProject = etBuProjectService.getById(id);
		if(etBuProject==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(etBuProject);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param etBuProject
    */
    //@RequiresPermissions("org.jeecg.modules.demo:业务项目:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EtBuProject etBuProject) {
		log.info("开始导出项目埋点事件...");
		return etBuProjectService.exportXls(request, etBuProject, EtEventMaterial2.class, "埋点事件");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("业务项目:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, EtBuProject.class);
    }

	 /**
	  * 业务项目关联事件功能，查看事件树
	  * @param request
	  * @return
	  */
	 @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
	 public Result<Map<String,Object>> queryTreeList(HttpServletRequest request) {
		 Result<Map<String, Object>> result = new Result<>();
		 //全部事件IDS
		 List<String> ids = new ArrayList<>();
		 try {
			 LambdaQueryWrapper<EtEvent> query = new LambdaQueryWrapper<>();
			 query.eq(EtEvent::getStatus, CommonConstant.ONLINE_FLAG);
			 query.orderByAsc(EtEvent::getScene).orderByAsc(EtEvent::getSorted).orderByDesc(EtEvent::getCreateTime);
			 List<EtEvent> list = etEventService.list(query);
			 ids = list.stream().map(EtEvent::getId).collect(Collectors.toList());
			 List<TreeModel> treeList = new ArrayList<>();
			 getTreeModelList(treeList, list, null);
			 Map<String, Object> resMap = new HashMap<>(5);
			 //全部树节点数据
			 resMap.put("treeList", treeList);
			 //全部树ids
			 resMap.put("ids", ids);
			 result.setResult(resMap);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
		 }
		 return result;
	 }

	 private void getTreeModelList(List<TreeModel> treeList, List<EtEvent> metaList, TreeModel temp) {
		 // 初始化树形节点
		 Map<String, List<EtEvent>> sceneEventMap = new HashMap<>(8);
		 List<EtEvent> etList;
		 for (EtEvent et : metaList) {
			 if (!sceneEventMap.containsKey(et.getScene())) {
				 etList = new ArrayList<>();
				 sceneEventMap.put(et.getScene(), etList);
			 }
			 etList = sceneEventMap.get(et.getScene());
			 etList.add(et);
		 }

		 // 将map转换为treeList
		 for (Map.Entry<String, List<EtEvent>> entry : sceneEventMap.entrySet()) {
			 String scene = entry.getKey();
			 List<EtEvent> eventList = entry.getValue();

			 // 将scene初始化成顶点tree
			 TreeModel parentTree = new TreeModel(scene, null, scene, 0, false);
			 treeList.add(parentTree);
			 List<TreeModel> childTreeModelList = new ArrayList<>(eventList.size());
			 for (EtEvent event : eventList) {
				 TreeModel childTree = new TreeModel(event.getId(), scene, String.format("%s[%s]", event.getZhName(), event.getName()), 0, true);
				 childTreeModelList.add(childTree);
			 }
			 parentTree.setChildren(childTreeModelList);
		 }
	 }
}
