package org.jeecg.modules.cg.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.cg.entity.CgDbConnectionInfo;
import org.jeecg.modules.cg.service.ICgDbConnectionInfoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.cg.service.UniversalConnectionService;
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

import static org.jeecg.common.util.PasswordUtil.aes256Decrypt;
import static org.jeecg.common.util.PasswordUtil.aes256Encrypt;

/**
 * @Description: CG数据库连接信息
 * @Author: jeecg-boot
 * @Date:   2024-09-26
 * @Version: V1.0
 */
@Api(tags="CG数据库连接信息")
@RestController
@RequestMapping("/cg/cgDbConnectionInfo")
@Slf4j
public class CgDbConnectionInfoController extends JeecgController<CgDbConnectionInfo, ICgDbConnectionInfoService> {
	@Autowired
	private ICgDbConnectionInfoService cgDbConnectionInfoService;

	@Autowired
	private UniversalConnectionService connectionService;
	
	/**
	 * 分页列表查询
	 *
	 * @param cgDbConnectionInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "CG数据库连接信息-分页列表查询")
	@ApiOperation(value="CG数据库连接信息-分页列表查询", notes="CG数据库连接信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<CgDbConnectionInfo>> queryPageList(CgDbConnectionInfo cgDbConnectionInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CgDbConnectionInfo> queryWrapper = QueryGenerator.initQueryWrapper(cgDbConnectionInfo, req.getParameterMap());
		//获取登录用户信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		// 若用户非admin，则排除业务线为“大数据平台”的数据源记录
		if (!"admin".equals(sysUser.getUsername())) {
			queryWrapper.lambda().notIn(CgDbConnectionInfo::getBuName, Collections.singletonList("大数据平台"));
		}
		Page<CgDbConnectionInfo> page = new Page<>(pageNo, pageSize);
		IPage<CgDbConnectionInfo> pageList = cgDbConnectionInfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param cgDbConnectionInfo
	 * @return
	 */
	@AutoLog(value = "CG数据库连接信息-添加")
	@ApiOperation(value="CG数据库连接信息-添加", notes="CG数据库连接信息-添加")
	@RequiresPermissions("org.jeecg.modules.demo:cg_db_connection_info:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CgDbConnectionInfo cgDbConnectionInfo) {
		cgDbConnectionInfoService.save(cgDbConnectionInfo);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cgDbConnectionInfo
	 * @return
	 */
	@AutoLog(value = "CG数据库连接信息-编辑")
	@ApiOperation(value="CG数据库连接信息-编辑", notes="CG数据库连接信息-编辑")
	@RequiresPermissions("org.jeecg.modules.demo:cg_db_connection_info:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CgDbConnectionInfo cgDbConnectionInfo) {
		cgDbConnectionInfoService.updateById(cgDbConnectionInfo);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "CG数据库连接信息-通过id删除")
	@ApiOperation(value="CG数据库连接信息-通过id删除", notes="CG数据库连接信息-通过id删除")
	@RequiresPermissions("org.jeecg.modules.demo:cg_db_connection_info:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		cgDbConnectionInfoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "CG数据库连接信息-批量删除")
	@ApiOperation(value="CG数据库连接信息-批量删除", notes="CG数据库连接信息-批量删除")
	@RequiresPermissions("org.jeecg.modules.demo:cg_db_connection_info:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cgDbConnectionInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "CG数据库连接信息-通过id查询")
	@ApiOperation(value="CG数据库连接信息-通过id查询", notes="CG数据库连接信息-通过id查询")
	@RequiresPermissions("org.jeecg.modules.demo:cg_db_connection_info:queryById")
	@GetMapping(value = "/queryById")
	public Result<CgDbConnectionInfo> queryById(@RequestParam(name="id",required=true) String id) {
		CgDbConnectionInfo cgDbConnectionInfo = cgDbConnectionInfoService.getById(id);
		if(cgDbConnectionInfo==null) {
			return Result.error("未找到对应数据");
		}
		// 解密密码
		cgDbConnectionInfo.setPassword(cgDbConnectionInfoService.showRealPassword(cgDbConnectionInfo.getPassword()));
		return Result.OK(cgDbConnectionInfo);
	}

	/**
	 * 显示真实密码
	 *
	 * @param id 数据库链接信息ID
	 */
	@AutoLog(value = "CG数据库连接信息-通过id查询真实密码")
	@ApiOperation(value="CG数据库连接信息-通过id查询真实密码", notes="CG数据库连接信息-通过id查询")
	@GetMapping(value = "/showRealPasswordById")
	@RequiresPermissions("org.jeecg.modules.demo:cg_db_connection_info:showRealPasswordById")
	public Result<CgDbConnectionInfo> showRealPasswordById(@RequestParam(name="id",required=true) String id) {
		CgDbConnectionInfo cgDbConnectionInfo = cgDbConnectionInfoService.getById(id);
		if(cgDbConnectionInfo == null) {
			return Result.error("未找到对应数据");
		}

		String realPassword = cgDbConnectionInfoService.showRealPassword(cgDbConnectionInfo.getPassword());
		cgDbConnectionInfo.setPassword(realPassword);
		return Result.OK(cgDbConnectionInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cgDbConnectionInfo
    */
	@AutoLog(value = "CG数据库连接信息-导出excel文件")
	@RequiresPermissions("org.jeecg.modules.demo:cg_db_connection_info:exportXls")
    @RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, CgDbConnectionInfo cgDbConnectionInfo) {
        return super.exportXls(request, cgDbConnectionInfo, CgDbConnectionInfo.class, "CG数据库连接信息");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
	@AutoLog(value = "CG数据库连接信息-导入excel文件")
	@RequiresPermissions("cg_db_connection_info:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CgDbConnectionInfo.class);
    }

	@ApiOperation(value="CG数据库连接信息-通过id测试服务是否正确配置", notes="CG数据库连接信息-通过id测试服务是否正确配置")
	@PostMapping("/testConnection")
	public Result<String> testConnection(@RequestParam(name="id",required=true) String id) {
		CgDbConnectionInfo cgDbConnectionInfo = cgDbConnectionInfoService.getById(id);
		if(cgDbConnectionInfo == null) {
			return Result.error("未找到对应数据");
		}

		String dbType = cgDbConnectionInfo.getConnectionType();
		String host = cgDbConnectionInfo.getHost();
		String port = cgDbConnectionInfo.getPort().toString();
		String username = cgDbConnectionInfo.getLogin();
		String password = cgDbConnectionInfoService.showRealPassword(cgDbConnectionInfo.getPassword());
		String dbName = cgDbConnectionInfo.getSchemaName();
		log.info("testConnection: dbType:{}, host:{}, port:{}, username:{}, password:{}, dbName:{}", dbType, host, port, username, password, dbName);
		String result = connectionService.testConnection(dbType, host, port, username, password, dbName);
		log.info("testConnection result: {}", result);
		if (StringUtils.contains(result, "successful")) {
			return Result.OK(result);
		}
		return Result.error(result);
	}

	@ApiOperation(value="CG数据库连接信息-导出JDBC URL复制链接", notes="CG数据库连接信息-导出JDBC URL复制链接")
	@GetMapping("/jdbcUrl")
	public Result<String> jdbcUrl(@RequestParam(name="id", required=true) String id) {
		CgDbConnectionInfo cgDbConnectionInfo = cgDbConnectionInfoService.getById(id);
		if(cgDbConnectionInfo == null) {
			return Result.error("未找到对应数据");
		}
		String dbType = cgDbConnectionInfo.getConnectionType();
		String host = cgDbConnectionInfo.getHost();
		String port = cgDbConnectionInfo.getPort().toString();
		String username = cgDbConnectionInfo.getLogin();
		String password = cgDbConnectionInfo.getPassword();
		String dbName = cgDbConnectionInfo.getSchemaName();
		// 检查必填字段
		if (StringUtils.isEmpty(dbType) || StringUtils.isEmpty(host) || StringUtils.isEmpty(password)) {
			return Result.error("检查输入信息错误，请填写必要完整信息再试!!!");
		}
		String jdbcUrl = connectionService.buildJdbcUrl(dbType, host, port, dbName);
		return Result.OK(jdbcUrl);
	}

	@ApiOperation(value="CG数据库连接信息-通过id测试服务是否正确配置，传入数据库连接信息", notes="CG数据库连接信息-通过id测试服务是否正确配置")
	@PostMapping("/testConnection2")
	public Result<String> testConnection(@RequestBody CgDbConnectionInfo cgDbConnectionInfo) {
		if(cgDbConnectionInfo == null) {
			return Result.error("未找到对应数据");
		}

		String dbType = cgDbConnectionInfo.getConnectionType();
		String host = cgDbConnectionInfo.getHost();
		String port = cgDbConnectionInfo.getPort().toString();
		String username = cgDbConnectionInfo.getLogin();
		String password = cgDbConnectionInfo.getPassword();
		String dbName = cgDbConnectionInfo.getSchemaName();
		// 检查必填字段
		if (StringUtils.isEmpty(dbType) || StringUtils.isEmpty(host) || StringUtils.isEmpty(password)) {
			return Result.error("检查输入信息错误，请填写必要完整信息再试!!!");
		}
		log.info("testConnection: dbType:{}, host:{}, port:{}, username:{}, password:{}, dbName:{}", dbType, host, port, username, password, dbName);
		String result = connectionService.testConnection(dbType, host, port, username, password, dbName);
		log.info("testConnection result: {}", result);
		if (StringUtils.contains(result, "successful")) {
			return Result.OK(result);
		}
		return Result.error(result);
	}
}
