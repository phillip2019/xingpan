package org.jeecg.modules.cg.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.PasswordUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: CG数据库连接信息
 * @Author: jeecg-boot
 * @Date:   2024-09-26
 * @Version: V1.0
 */
@Data
@TableName("cg_db_connection_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cg_db_connection_info对象", description="CG数据库连接信息")
public class CgDbConnectionInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "编号")
    private java.lang.String id;
	/**业务线名称*/
    @Dict(dicCode = "bu_name")
    @Excel(name = "业务线", width = 15, dicCode = "bu_name")
    @ApiModelProperty(value = "业务线名称")
    private java.lang.String buName;
    /**系统名称*/
    @Excel(name = "系统名称", width = 15)
    @ApiModelProperty(value = "系统名称")
    private java.lang.String sys;
	/**数据库连接ID(英文名)*/
	@Excel(name = "Con ID", width = 15)
    @ApiModelProperty(value = "数据库连接ID(英文名)")
    private java.lang.String connectionId;
	/**状态*/
    @Dict(dicCode = "valid_status")
    @Excel(name = "状态", width = 15, dicCode = "valid_status")
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
    /**数据源连接状态*/
    @Dict(dicCode = "connect_status")
    @Excel(name = "连接状态", width = 15, dicCode = "connect_status")
    @ApiModelProperty(value = "连接状态")
    private java.lang.Integer connectStatus;
	/**数据库连接类型*/
    @Dict(dicCode = "connection_type")
    @Excel(name = "数据库类型", width = 15, dicCode = "connection_type")
    @ApiModelProperty(value = "数据库连接类型")
    private java.lang.String connectionType;
    /**数据库版本*/
    @Excel(name = "引擎版本", width = 15)
    @ApiModelProperty(value = "引擎版本")
    private java.lang.String connectionTypeVersion;
	/**数据库连接描述内容*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "数据库连接描述内容")
    private java.lang.String description;
	/**数据库连接主机*/
	@Excel(name = "HOST", width = 15)
    @ApiModelProperty(value = "数据库连接主机")
    private java.lang.String host;
	/**数据库连接DB*/
	@Excel(name = "schema", width = 15)
    @ApiModelProperty(value = "数据库连接DB")
    private java.lang.String schemaName;
	/**用户名*/
	@Excel(name = "用户名", width = 15)
    @ApiModelProperty(value = "用户名")
    private java.lang.String login;
	/**密码*/
	@Excel(name = "密码", width = 15)
    @ApiModelProperty(value = "密码")
    private java.lang.String password;
	/**数据库连接端口*/
	@Excel(name = "数据库连接端口", width = 15)
    @ApiModelProperty(value = "数据库连接端口")
    private java.lang.Integer port;
	/**额外连接信息*/
	@Excel(name = "额外连接信息", width = 15)
    @ApiModelProperty(value = "额外连接信息")
    private java.lang.String extra;
    /**数据库对应系统地址，若有多个，逗号分割*/
    @Excel(name = "WEB系统", width = 15)
    @ApiModelProperty(value = "WEB系统")
    private java.lang.String webUrlArr;
    /**数据库缩写名称，提供给入数仓使用*/
    @Excel(name = "库缩写名称", width = 15)
    @ApiModelProperty(value = "库缩写名称")
    private java.lang.String dbAbbreviation;
	/**版本，记录当前修改版本，记录历史记录*/
	@Excel(name = "版本", width = 15)
    @ApiModelProperty(value = "版本，记录当前修改版本，记录历史记录")
    private java.lang.Integer version;
	/**连接超时时间*/
	@Excel(name = "连接超时", width = 15)
    @ApiModelProperty(value = "连接超时时间")
    private java.lang.Integer connectionTimeout;
	/**queryTimeout*/
	@Excel(name = "查询超时", width = 15)
    @ApiModelProperty(value = "queryTimeout")
    private java.lang.Integer queryTimeout;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;

    @SneakyThrows({Exception.class})
    public String setPassword(String password, String base64SecretKey) {
        // 若是新增或密码修改，则需要重新加密密码
        this.password = PasswordUtil.aes256Encrypt(password, base64SecretKey);
        return this.password;
    }

    /**
     * 将数据库连接信息转换为Airflow的连接JSON信息
     * @param conn 连接实例
     * @param base64SecretKey 密钥
     * @return airflow连接信息
     * @throws Exception 密码解密异常
     */
    public static String toAirflowConnectionJson(CgDbConnectionInfo conn, String base64SecretKey) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);  // 不转义非 ASCII 字符
        return objectMapper.writeValueAsString(CgDbConnectionInfo.toAirflowConnection(conn, base64SecretKey));
    }

    /**
     * 将数据库连接信息转换为Airflow的连接信息
     * @param conn 连接实例
     * @param base64SecretKey 密钥
     * @return airflow连接信息
     * @throws Exception 密码解密异常
     */
    private static java.util.Map<String, Object> toAirflowConnection(CgDbConnectionInfo conn, String base64SecretKey) throws Exception {
        java.util.Map<String, Object> connectionMap = new java.util.HashMap<>();
        connectionMap.put("connection_id", conn.connectionId);
        connectionMap.put("conn_type", conn.connectionType);
        connectionMap.put("host", conn.host);
        connectionMap.put("schema", conn.schemaName);
        connectionMap.put("login", conn.login);
        connectionMap.put("password", PasswordUtil.aes256Decrypt(conn.password, base64SecretKey));
        connectionMap.put("port", conn.port);
        connectionMap.put("description", conn.description);
        connectionMap.put("extra", conn.extra);
        return connectionMap;
    }
}
