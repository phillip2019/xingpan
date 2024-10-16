package org.jeecg.modules.ma.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.ma.entity.MaTaiKaShop;
import org.jeecg.modules.ma.service.IMaTaiKaShopService;

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
 * @Description: ma_tai_ka_shop
 * @Author: jeecg-boot
 * @Date:   2023-04-03
 * @Version: V1.0
 */
@Api(tags="商铺台卡之店铺二维码")
@RestController
@RequestMapping("/ma/maTaiKaShop")
@Slf4j
public class MaTaiKaShopController extends JeecgController<MaTaiKaShop, IMaTaiKaShopService> {
	@Autowired
	private IMaTaiKaShopService maTaiKaShopService;
	
	/**
	 * 分页列表查询
	 *
	 * @param maTaiKaShop
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "ma_tai_ka_shop-分页列表查询")
	@ApiOperation(value="ma_tai_ka_shop-分页列表查询", notes="ma_tai_ka_shop-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MaTaiKaShop>> queryPageList(MaTaiKaShop maTaiKaShop,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MaTaiKaShop> queryWrapper = QueryGenerator.initQueryWrapper(maTaiKaShop, req.getParameterMap());
		Page<MaTaiKaShop> page = new Page<MaTaiKaShop>(pageNo, pageSize);
		IPage<MaTaiKaShop> pageList = maTaiKaShopService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param maTaiKaShop
	 * @return
	 */
	@AutoLog(value = "ma_tai_ka_shop-添加")
	@ApiOperation(value="ma_tai_ka_shop-添加", notes="ma_tai_ka_shop-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_tai_ka_shop:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody MaTaiKaShop maTaiKaShop) {
		maTaiKaShopService.save(maTaiKaShop);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param maTaiKaShop
	 * @return
	 */
	@AutoLog(value = "ma_tai_ka_shop-编辑")
	@ApiOperation(value="ma_tai_ka_shop-编辑", notes="ma_tai_ka_shop-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_tai_ka_shop:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody MaTaiKaShop maTaiKaShop) {
		maTaiKaShopService.updateById(maTaiKaShop);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ma_tai_ka_shop-通过id删除")
	@ApiOperation(value="ma_tai_ka_shop-通过id删除", notes="ma_tai_ka_shop-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_tai_ka_shop:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		maTaiKaShopService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "ma_tai_ka_shop-批量删除")
	@ApiOperation(value="ma_tai_ka_shop-批量删除", notes="ma_tai_ka_shop-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_tai_ka_shop:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.maTaiKaShopService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "ma_tai_ka_shop-通过id查询")
	@ApiOperation(value="ma_tai_ka_shop-通过id查询", notes="ma_tai_ka_shop-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MaTaiKaShop> queryById(@RequestParam(name="id",required=true) String id) {
		MaTaiKaShop maTaiKaShop = maTaiKaShopService.getById(id);
		if(maTaiKaShop==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(maTaiKaShop);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param maTaiKaShop
    */
    //@RequiresPermissions("org.jeecg.modules.demo:ma_tai_ka_shop:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MaTaiKaShop maTaiKaShop) {
        return super.exportXls(request, maTaiKaShop, MaTaiKaShop.class, "ma_tai_ka_shop");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ma_tai_ka_shop:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MaTaiKaShop.class);
    }

}
