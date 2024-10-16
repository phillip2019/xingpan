package org.jeecg.modules.ma.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.ma.entity.MaPositionShop;
import org.jeecg.modules.ma.service.IMaPositionShopService;

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
 * @Description: 点位店铺
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
@Api(tags="点位店铺")
@RestController
@RequestMapping("/ma/positionShop")
@Slf4j
public class MaPositionShopController extends JeecgController<MaPositionShop, IMaPositionShopService> {
	@Autowired
	private IMaPositionShopService maPositionShopService;
	
	/**
	 * 分页列表查询
	 *
	 * @param maPositionShop
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "点位店铺-分页列表查询")
	@ApiOperation(value="点位店铺-分页列表查询", notes="点位店铺-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MaPositionShop>> queryPageList(MaPositionShop maPositionShop,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MaPositionShop> queryWrapper = QueryGenerator.initQueryWrapper(maPositionShop, req.getParameterMap());
		Page<MaPositionShop> page = new Page<MaPositionShop>(pageNo, pageSize);
		IPage<MaPositionShop> pageList = maPositionShopService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param maPositionShop
	 * @return
	 */
	@AutoLog(value = "点位店铺-添加")
	@ApiOperation(value="点位店铺-添加", notes="点位店铺-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_position_shop:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody MaPositionShop maPositionShop) {
		maPositionShopService.save(maPositionShop);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param maPositionShop
	 * @return
	 */
	@AutoLog(value = "点位店铺-编辑")
	@ApiOperation(value="点位店铺-编辑", notes="点位店铺-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_position_shop:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody MaPositionShop maPositionShop) {
		maPositionShopService.updateById(maPositionShop);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "点位店铺-通过id删除")
	@ApiOperation(value="点位店铺-通过id删除", notes="点位店铺-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_position_shop:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		maPositionShopService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "点位店铺-批量删除")
	@ApiOperation(value="点位店铺-批量删除", notes="点位店铺-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_position_shop:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.maPositionShopService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "点位店铺-通过id查询")
	@ApiOperation(value="点位店铺-通过id查询", notes="点位店铺-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MaPositionShop> queryById(@RequestParam(name="id",required=true) String id) {
		MaPositionShop maPositionShop = maPositionShopService.getById(id);
		if(maPositionShop==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(maPositionShop);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param maPositionShop
    */
    //@RequiresPermissions("org.jeecg.modules.demo:ma_position_shop:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MaPositionShop maPositionShop) {
        return super.exportXls(request, maPositionShop, MaPositionShop.class, "点位店铺");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ma_position_shop:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MaPositionShop.class);
    }

}
