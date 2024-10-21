package org.jeecg.modules.cg.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.cg.entity.CgDbConnectionInfo;
import org.jeecg.modules.cg.service.ICgDbConnectionInfoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.cg.service.UniversalConnectionService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

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
		return testConnection(dbType, host, port, username, password, dbName);
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
		return testConnection(dbType, host, port, username, password, dbName);
	}

	@NotNull
	private Result<String> testConnection(String dbType, String host, String port, String username, String password, String dbName) {
		log.info("testConnection: dbType:{}, host:{}, port:{}, username:{}, password:{}, dbName:{}", dbType, host, port, username, password, dbName);
		String result = connectionService.testConnection(dbType, host, port, username, password, dbName);
		log.info("testConnection result: {}", result);
		if (StringUtils.contains(result, "successful!")) {
			return Result.OK(result);
		}
		return Result.error(result);
	}

	@ApiOperation(value="CG数据库连接信息-同步Airflow连接信息", notes="CG数据库连接信息-同步Airflow连接信息")
	@GetMapping("/syncAirflowConnection")
	public Result<String> syncAirflowConnection(@RequestParam(name="id", required=true) String id) {
		CgDbConnectionInfo cgDbConnectionInfo = cgDbConnectionInfoService.getById(id);
		if(cgDbConnectionInfo == null) {
			return Result.error("未找到对应数据");
		}
		String rst = cgDbConnectionInfoService.syncAirflowConnection(cgDbConnectionInfo);
		// 若rst包含"成功"，则返回OK
		if (StringUtils.contains(rst, "成功")) {
			return Result.OK(rst);
		}
		return Result.error(rst);
	}

	/**
	 * 批量测试服务是否正确配置及更新库的状态
	 */
	@ApiOperation(value="CG数据库连接信息-批量测试服务是否可用", notes="CG数据库连接信息-批量测试服务是否可用")
	@GetMapping("/batchTestConnection")
	public Result<String> batchTestConnection(@RequestParam(name="ids", required=false) String ids) {
		List<CgDbConnectionInfo> cgDbConnectionInfoList;
		List<CgDbConnectionInfo> preUpdateDbConnectionInfoList = new ArrayList<>();
		if (StringUtils.isEmpty(ids)) {
			log.info("batchTestConnection: ids is empty, query all data");
			// 查询所有非删除状态的数据、非http数据源的数据
			cgDbConnectionInfoList = cgDbConnectionInfoService.list(new QueryWrapper<CgDbConnectionInfo>().eq("status", "1").ne("connection_type", "http"));
		} else {
			log.info("batchTestConnection: ids is not empty, query data by ids: {}", ids);
			cgDbConnectionInfoList = cgDbConnectionInfoService.listByIds(Arrays.asList(ids.split(",")));
		}
		// 循环测试数据库连接
		if (cgDbConnectionInfoList.isEmpty()) {
			return Result.error("未找到对应数据源");
		}
		log.info("batchTestConnection: cgDbConnectionInfoList size: {}", cgDbConnectionInfoList.size());
		int count = 0;
		for (CgDbConnectionInfo cgDbConnectionInfo : cgDbConnectionInfoList) {
			log.info("batchTestConnection: test connection: {}, process position {}, total size: {}...", cgDbConnectionInfo, count++, cgDbConnectionInfoList.size());
			String dbType = cgDbConnectionInfo.getConnectionType();
			String host = cgDbConnectionInfo.getHost();
			String port = cgDbConnectionInfo.getPort().toString();
			String username = cgDbConnectionInfo.getLogin();
			String password = cgDbConnectionInfoService.showRealPassword(cgDbConnectionInfo.getPassword());
			String dbName = cgDbConnectionInfo.getSchemaName();
			// 检查必填字段
			if (StringUtils.isEmpty(dbType) || StringUtils.isEmpty(host) || StringUtils.isEmpty(password)) {
				continue;
			}
			Result<String> rst = testConnection(dbType, host, port, username, password, dbName);
			cgDbConnectionInfo.setConnectStatus("0");
			if (rst.isSuccess()) {
				cgDbConnectionInfo.setConnectStatus("1");
			}
			preUpdateDbConnectionInfoList.add(cgDbConnectionInfo);
		}
		if (!preUpdateDbConnectionInfoList.isEmpty()) {
			cgDbConnectionInfoService.updateBatchById(preUpdateDbConnectionInfoList);
		}

		return Result.OK("批量测试服务是否正确配置完成");
	}

	/**
	 * 批量更新数据源版本
	 */
	@ApiOperation(value="CG数据库连接信息-批量更新数据源版本", notes="CG数据库连接信息-批量更新数据源版本")
	@GetMapping("/batchUpdateVersion")
	public Result<String> batchUpdateVersion(@RequestParam(name="ids", required=false) String ids) {
		// 检测数据源引擎版本
		List<CgDbConnectionInfo> cgDbConnectionInfoList;
		List<CgDbConnectionInfo> preUpdateDbConnectionInfoList = new ArrayList<>();
		if (StringUtils.isEmpty(ids)) {
			log.info("batchUpdateConnectionVersion: ids is empty, query all data");
			// 查询所有非删除状态的数据、非http数据源的数据
			cgDbConnectionInfoList = cgDbConnectionInfoService.list(new QueryWrapper<CgDbConnectionInfo>()
					.eq("status", "1")
					.eq("connect_status", "1")
					// connection_type_version为空的
					.isNull("connection_type_version")
					.ne("connection_type", "http"));
		} else {
			log.info("batchTestConnection: ids is not empty, query data by ids: {}", ids);
			cgDbConnectionInfoList = cgDbConnectionInfoService.listByIds(Arrays.asList(ids.split(",")));
		}
		// 循环测试数据库连接
		if (cgDbConnectionInfoList.isEmpty()) {
			return Result.error("未找到对应数据源");
		}
		log.info("batchUpdateConnectionVersion: cgDbConnectionInfoList size: {}", cgDbConnectionInfoList.size());
		int count = 0;
		for (CgDbConnectionInfo cgDbConnectionInfo : cgDbConnectionInfoList) {
			log.info("batchUpdateConnectionVersion: connection: {}, process position {}, total size: {}...", cgDbConnectionInfo, count++, cgDbConnectionInfoList.size());
			String dbType = cgDbConnectionInfo.getConnectionType();
			String host = cgDbConnectionInfo.getHost();
			String port = cgDbConnectionInfo.getPort().toString();
			String username = cgDbConnectionInfo.getLogin();
			String password = cgDbConnectionInfoService.showRealPassword(cgDbConnectionInfo.getPassword());
			// 检查必填字段
			if (StringUtils.isEmpty(dbType) || StringUtils.isEmpty(host) || StringUtils.isEmpty(password)) {
				continue;
			}
			String rst = connectionService.checkEngineVersion(dbType, host, port, username, password);
			if (StringUtils.isNotEmpty(rst)) {
				cgDbConnectionInfo.setConnectionTypeVersion(rst);
				preUpdateDbConnectionInfoList.add(cgDbConnectionInfo);
			}
		}
		if (!preUpdateDbConnectionInfoList.isEmpty()) {
			cgDbConnectionInfoService.updateBatchById(preUpdateDbConnectionInfoList);
		}
		return Result.OK(String.format("批量更新数据源版本完成, 更新条数: %d", preUpdateDbConnectionInfoList.size()));
	}
}
