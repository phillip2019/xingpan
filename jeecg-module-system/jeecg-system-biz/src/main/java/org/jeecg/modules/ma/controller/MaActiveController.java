package org.jeecg.modules.ma.controller;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.ma.entity.MaActive;
import org.jeecg.modules.ma.entity.MaActiveYlbMaterial;
import org.jeecg.modules.ma.entity.MaActiveTaiKaMaterial;
import org.jeecg.modules.ma.service.IMaActiveService;

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
 * @Description: 活动
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
@Api(tags="活动")
@RestController
@RequestMapping("/ma/active")
@Slf4j
public class MaActiveController extends JeecgController<MaActive, IMaActiveService> {
	@Autowired
	private IMaActiveService maActiveService;

	 @Resource
	 private JeecgBaseConfig jeecgBaseConfig;
	/**upload
	 * 分页列表查询
	 *
	 * @param maActive
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "活动-分页列表查询")
	@ApiOperation(value="活动-分页列表查询", notes="活动-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MaActive>> queryPageList(MaActive maActive,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MaActive> queryWrapper = QueryGenerator.initQueryWrapper(maActive, req.getParameterMap());
		Page<MaActive> page = new Page<MaActive>(pageNo, pageSize);
		IPage<MaActive> pageList = maActiveService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param maActive
	 * @return
	 */
	@AutoLog(value = "活动-添加")
	@ApiOperation(value="活动-添加", notes="活动-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_active:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody MaActive maActive) {
		maActiveService.save(maActive);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param maActive
	 * @return
	 */
	@AutoLog(value = "活动-编辑")
	@ApiOperation(value="活动-编辑", notes="活动-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_active:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody MaActive maActive) {
		maActiveService.updateById(maActive);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "活动-通过id删除")
	@ApiOperation(value="活动-通过id删除", notes="活动-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_active:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		maActiveService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "活动-批量删除")
	@ApiOperation(value="活动-批量删除", notes="活动-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:ma_active:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.maActiveService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "活动-通过id查询")
	@ApiOperation(value="活动-通过id查询", notes="活动-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MaActive> queryById(@RequestParam(name="id",required=true) String id) {
		MaActive maActive = maActiveService.getById(id);
		if(maActive==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(maActive);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param maActive
    */
    //@RequiresPermissions("org.jeecg.modules.demo:ma_active:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MaActive maActive) {
        return super.exportXls(request, maActive, MaActive.class, "活动");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ma_active:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MaActive.class);
    }


	 /**
	  * 通过excel导入易拉宝数据
	  * @param request
	  * @param response
	  * @return
	  */
	 //@RequiresPermissions("ma_active:importExcel")
	 @RequestMapping(value = "/importYLBExcel", method = RequestMethod.POST)
	 public Result<?> importYlbExcel(@NotNull(message = "活动编号必填") @RequestParam("id") Long activeId, HttpServletRequest request, HttpServletResponse response) {
	 	log.info("开始导入活动编号： {}易拉宝物料", activeId);
		 return maActiveService.importYlbExcel(activeId, request, response, MaActiveYlbMaterial.class);
	 }

	 /**
	  * 导出易拉宝公众号二维码数据
	  * @param request 请求
	  * @param response 返回内容
	  * @return
	  */
	 //@RequiresPermissions("ma_active:exportYLBQrCode")
	 @RequestMapping(value = "/exportYLBQrCode")
	 public void exportYlbQrCode(@NotNull(message = "活动编号必填") @RequestParam("id") Long activeId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		 String downloadName = "易拉宝二维码.zip";
		 //服务器存储地址
		 String srcSource = jeecgBaseConfig.getPath().getUpload() + File.separator + "qrCode" + File.separator + "ylb";
		 log.info("开始下载活动: {} 微信公众号带参二维码图片到目录: {}中", activeId, srcSource);
		 maActiveService.downloadActiveQrCode(activeId, srcSource);
		 String targetActiveSrcSource = Paths.get(srcSource, String.valueOf(activeId)).toString();
		 //将文件进行打包下载
		 try (OutputStream out =  response.getOutputStream()) {
		 	String targetFile = targetActiveSrcSource + ".zip";
			 log.info("活动: {} 开始压缩微信公众号带参二维码目录: {} 到文件: {}中", activeId, targetActiveSrcSource, targetFile);
			 // 先将文件压缩成active_id.zip，再删除此文件
			 ZipUtil.zip(targetActiveSrcSource, targetFile);
			 //将目标文件打包成zip导出
			 File file = new File(targetFile);
			 // 重置返回内容
			 response.reset();
			 try(FileInputStream fis = FileUtils.openInputStream(file)) {
				 IOUtils.copy(fis, out);
			 }
			 // 删除压缩好的包
			 FileUtil.del(file);
			 // 此处没用，前端会重新覆盖
			 response.setHeader("Content-Disposition","attachment;fileName="+downloadName);
			 response.setContentType("application/octet-stream;charset=UTF-8");
			 out.flush();
		 } catch (Exception e) {
			 log.error("目录: {} 打包下载失败，请重新再试!", targetActiveSrcSource, e);
		 }
	 }

	 /**
	  * 通过excel导入商位台卡店铺数据
	  * @param request
	  * @param response
	  * @return
	  */
	 //@RequiresPermissions("ma_active:importExcel")
	 @RequestMapping(value = "/importTaiKaExcel", method = RequestMethod.POST)
	 public Result<?> importTaiKaExcel(@NotNull(message = "活动编号必填") @RequestParam("id") Long activeId, HttpServletRequest request, HttpServletResponse response) {
		 log.info("开始导入活动编号： {}商位台卡店铺物料", activeId);
		 return maActiveService.importTaiKaExcel(activeId, request, response, MaActiveTaiKaMaterial.class);
	 }

	 /**
	  * 导出商位台卡店铺公众号二维码数据
	  * @param request 请求
	  * @param response 返回内容
	  * @return
	  */
	 //@RequiresPermissions("ma_active:exportTaiKaQrCode")
	 @RequestMapping(value = "/exportTaiKaQrCode")
	 public void exportTaiKaQrCode(@NotNull(message = "活动编号必填") @RequestParam("id") Long activeId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		 String downloadName = "商位台卡店铺二维码.zip";
		 //服务器存储地址
		 String srcSource = jeecgBaseConfig.getPath().getUpload() + File.separator + "qrCode" + File.separator + "taika";
		 log.info("开始下载活动: {} 台卡店铺微信公众号带参二维码图片到目录: {}中", activeId, srcSource);
		 maActiveService.downloadActiveTaiKaQrCode(activeId, srcSource);
		 String targetActiveSrcSource = Paths.get(srcSource, String.valueOf(activeId)).toString();
		 //将文件进行打包下载
		 try (OutputStream out =  response.getOutputStream()) {
			 String targetFile = targetActiveSrcSource + ".zip";
			 log.info("活动: {} 开始压缩微信公众号带参二维码目录: {} 到文件: {}中", activeId, targetActiveSrcSource, targetFile);
			 // 先将文件压缩成active_id.zip，再删除此文件
			 ZipUtil.zip(targetActiveSrcSource, targetFile);
			 //将目标文件打包成zip导出
			 File file = new File(targetFile);
			 // 重置返回内容
			 response.reset();
			 try(FileInputStream fis = FileUtils.openInputStream(file)) {
				 IOUtils.copy(fis, out);
			 }
			 // 删除压缩好的包
			 FileUtil.del(file);
			 // 此处没用，前端会重新覆盖
			 response.setHeader("Content-Disposition","attachment;fileName="+downloadName);
			 response.setContentType("application/octet-stream;charset=UTF-8");
			 out.flush();
		 } catch (Exception e) {
			 log.error("目录: {} 打包下载失败，请重新再试!", targetActiveSrcSource, e);
		 }
	 }
}
