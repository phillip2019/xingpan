package org.jeecg.modules.et.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 埋点日志
 * @Author: jeecg-boot
 * @Date:   2022-11-22
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="埋点", description="埋点日志")
public class EtChinagoods implements Serializable {
    private static final long serialVersionUID = 1L;

	/**trackId*/
	@Excel(name = "trackId", width = 15)
    @ApiModelProperty(value = "trackId")
    @TableId(type = IdType.INPUT)
    private Long trackId;
	/**distinctId*/
	@Excel(name = "distinctId", width = 15)
    @ApiModelProperty(value = "distinctId")
    private String distinctId;
	/**lib*/
	@Excel(name = "lib", width = 15)
    @ApiModelProperty(value = "lib")
    private String lib;
	/**event*/
	@Excel(name = "event", width = 15)
    @ApiModelProperty(value = "event")
    private String event;
	/**type*/
	@Excel(name = "type", width = 15)
    @ApiModelProperty(value = "type")
    private String type;
	/**allJson*/
	@Excel(name = "allJson", width = 15)
    @ApiModelProperty(value = "allJson")
    private String allJson;
	/**host*/
	@Excel(name = "host", width = 15)
    @ApiModelProperty(value = "host")
    private String host;
	/**userAgent*/
	@Excel(name = "userAgent", width = 15)
    @ApiModelProperty(value = "userAgent")
    private String userAgent;
	/**uaPlatform*/
	@Excel(name = "uaPlatform", width = 15)
    @ApiModelProperty(value = "uaPlatform")
    private String uaPlatform;
	/**uaBrowser*/
	@Excel(name = "uaBrowser", width = 15)
    @ApiModelProperty(value = "uaBrowser")
    private String uaBrowser;
	/**uaVersion*/
	@Excel(name = "uaVersion", width = 15)
    @ApiModelProperty(value = "uaVersion")
    private String uaVersion;
	/**uaLanguage*/
	@Excel(name = "uaLanguage", width = 15)
    @ApiModelProperty(value = "uaLanguage")
    private String uaLanguage;
	/**acceptEncoding*/
	@Excel(name = "acceptEncoding", width = 15)
    @ApiModelProperty(value = "acceptEncoding")
    private String acceptEncoding;
	/**acceptLanguage*/
	@Excel(name = "acceptLanguage", width = 15)
    @ApiModelProperty(value = "acceptLanguage")
    private String acceptLanguage;
	/**ip*/
	@Excel(name = "ip", width = 15)
    @ApiModelProperty(value = "ip")
    private String ip;
	/**ipCity*/
	@Excel(name = "ipCity", width = 15)
    @ApiModelProperty(value = "ipCity")
    private String ipCity;
	/**url*/
	@Excel(name = "url", width = 15)
    @ApiModelProperty(value = "url")
    private String url;
	/**referrer*/
	@Excel(name = "referrer", width = 15)
    @ApiModelProperty(value = "referrer")
    private String referrer;
	/**remark*/
	@Excel(name = "remark", width = 15)
    @ApiModelProperty(value = "remark")
    private String remark;
	/**createdAt*/
	@Excel(name = "createdAt", width = 15)
    @ApiModelProperty(value = "createdAt")
    private String createdAt;
	/**date*/
	@Excel(name = "date", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "date")
    private Date date;
	/**hour*/
	@Excel(name = "hour", width = 15)
    @ApiModelProperty(value = "hour")
    private Integer hour;
    /**project*/
    @TableField(exist = false)
    private transient String project;
    /**platformType*/
    @TableField(exist = true)
    private String platformType;

    @TableField(exist = false)
    private String anonymousId;
}
