package org.jeecg.modules.ma.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.ma.entity.MaPosition;
import org.jeecg.modules.ma.entity.MaPositionAddress;
import org.jeecg.modules.ma.mapper.MaPositionMapper;
import org.jeecg.modules.ma.service.IMaPositionAddressService;

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
 * @Description: 点位位置
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
@Api(tags="点位位置")
@RestController
@RequestMapping("/ma/positionAddress")
@Slf4j
public class MaPositionAddressController extends JeecgController<MaPositionAddress, IMaPositionAddressService> {
	@Autowired
	private IMaPositionAddressService maPositionAddressService;

	 @Autowired
	 private MaPositionMapper positionMapper;
	
	/**
	 * 分页列表查询
	 *
	 * @param maPositionAddress
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "点位位置-分页列表查询")
	@ApiOperation(value="点位位置-分页列表查询", notes="点位位置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MaPositionAddress>> queryPageList(MaPositionAddress maPositionAddress,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Map<String, String[]> requestMap = new HashMap<>(req.getParameterMap().size());
		requestMap.putAll(req.getParameterMap());
		List<String> positionIdList = null;
		// 若maPositionAddress中positionSeqNo非空，则转换positionSeqNo为positionId
		if (StringUtils.isNotBlank(maPositionAddress.getPositionSeqNo())) {
			QueryWrapper<MaPosition> maPositionQueryWrapper = new QueryWrapper<>();
			maPositionQueryWrapper.eq("seq_no", maPositionAddress.getPositionSeqNo());
			maPositionQueryWrapper.select("id");
			MaPosition maPosition = positionMapper.selectOne(maPositionQueryWrapper);
			List<MaPosition> positionList = positionMapper.selectList(maPositionQueryWrapper);
			if (positionList == null || positionList.isEmpty()) {
				return Result.error(String.format("序号: %s查询为空，请检查序号", maPositionAddress.getPositionSeqNo()));
			}
			positionIdList = positionList.stream().map(MaPosition::getId).collect(Collectors.toList());
		}
		QueryWrapper<MaPositionAddress> queryWrapper = QueryGenerator.initQueryWrapper(maPositionAddress, requestMap);
		if (positionIdList != null && !positionIdList.isEmpty()) {
			queryWrapper.in("position_id", positionIdList);
		}
		Page<MaPositionAddress> page = new Page<MaPositionAddress>(pageNo, pageSize);
		IPage<MaPositionAddress> pageList = maPositionAddressService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param maPositionAddress
	 * @return
	 */
	@AutoLog(value = "点位位置-添加")
	@ApiOperation(value="点位位置-添加", notes="点位位置-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_position_address:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody MaPositionAddress maPositionAddress) {
		maPositionAddressService.save(maPositionAddress);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param maPositionAddress
	 * @return
	 */
	@AutoLog(value = "点位位置-编辑")
	@ApiOperation(value="点位位置-编辑", notes="点位位置-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_position_address:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody MaPositionAddress maPositionAddress) {
		maPositionAddressService.updateById(maPositionAddress);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "点位位置-通过id删除")
	@ApiOperation(value="点位位置-通过id删除", notes="点位位置-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_position_address:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		maPositionAddressService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "点位位置-批量删除")
	@ApiOperation(value="点位位置-批量删除", notes="点位位置-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_position_address:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.maPositionAddressService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "点位位置-通过id查询")
	@ApiOperation(value="点位位置-通过id查询", notes="点位位置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MaPositionAddress> queryById(@RequestParam(name="id",required=true) String id) {
		MaPositionAddress maPositionAddress = maPositionAddressService.getById(id);
		if(maPositionAddress==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(maPositionAddress);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param maPositionAddress
    */
    //@RequiresPermissions("org.jeecg.modules.demo:ma_position_address:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MaPositionAddress maPositionAddress) {
        return super.exportXls(request, maPositionAddress, MaPositionAddress.class, "点位位置");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ma_position_address:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MaPositionAddress.class);
    }

}
