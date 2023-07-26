package org.jeecg.modules.demo.org.jeecg.et.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: et_platform_site_code
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Data
@TableName("et_platform_site_code")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="et_platform_site_code对象", description="et_platform_site_code")
public class EtPlatformSiteCode implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID，自增*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID，自增")
    private java.lang.Integer id;
	/**站点类型*/
	@Excel(name = "站点类型", width = 15)
    @ApiModelProperty(value = "站点类型")
    private java.lang.String platformSiteType;
	/**站点中文名*/
	@Excel(name = "站点中文名", width = 15)
    @ApiModelProperty(value = "站点中文名")
    private java.lang.String platformSiteZhName;
	/**埋点project名称*/
	@Excel(name = "埋点project名称", width = 15)
    @ApiModelProperty(value = "埋点project名称")
    private java.lang.String project;
	/**平台站点*/
	@Excel(name = "平台站点", width = 15)
    @ApiModelProperty(value = "平台站点")
    private java.lang.String platformSite;
	/**平台语言*/
	@Excel(name = "平台语言", width = 15)
    @ApiModelProperty(value = "平台语言")
    private java.lang.String platformLang;
	/**站点网址*/
	@Excel(name = "站点网址", width = 15)
    @ApiModelProperty(value = "站点网址")
    private java.lang.String platformUrl;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
}
