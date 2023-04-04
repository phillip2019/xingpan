package org.jeecg.modules.ma.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.ma.entity.MaPosition;
import org.jeecg.modules.ma.service.IMaPositionService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 活动点位
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
@Api(tags="活动点位")
@RestController
@RequestMapping("/ma/position")
@Slf4j
public class MaPositionController extends JeecgController<MaPosition, IMaPositionService> {
	@Autowired
	private IMaPositionService maPositionService;
	
	/**
	 * 分页列表查询
	 *
	 * @param maPosition
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "活动点位-分页列表查询")
	@ApiOperation(value="活动点位-分页列表查询", notes="活动点位-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MaPosition>> queryPageList(MaPosition maPosition,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MaPosition> queryWrapper = QueryGenerator.initQueryWrapper(maPosition, req.getParameterMap());
		Page<MaPosition> page = new Page<MaPosition>(pageNo, pageSize);
		IPage<MaPosition> pageList = maPositionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param maPosition
	 * @return
	 */
	@AutoLog(value = "活动点位-添加")
	@ApiOperation(value="活动点位-添加", notes="活动点位-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_position:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody MaPosition maPosition) {
		maPositionService.save(maPosition);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param maPosition
	 * @return
	 */
	@AutoLog(value = "活动点位-编辑")
	@ApiOperation(value="活动点位-编辑", notes="活动点位-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_position:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody MaPosition maPosition) {
		maPositionService.updateById(maPosition);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "活动点位-通过id删除")
	@ApiOperation(value="活动点位-通过id删除", notes="活动点位-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_position:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		maPositionService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "活动点位-批量删除")
	@ApiOperation(value="活动点位-批量删除", notes="活动点位-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_position:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.maPositionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "活动点位-通过id查询")
	@ApiOperation(value="活动点位-通过id查询", notes="活动点位-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MaPosition> queryById(@RequestParam(name="id",required=true) String id) {
		MaPosition maPosition = maPositionService.getById(id);
		if(maPosition==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(maPosition);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param maPosition
    */
    //@RequiresPermissions("org.jeecg.modules.demo:ma_position:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MaPosition maPosition) {
        return super.exportXls(request, maPosition, MaPosition.class, "活动点位");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ma_position:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MaPosition.class);
    }

}
