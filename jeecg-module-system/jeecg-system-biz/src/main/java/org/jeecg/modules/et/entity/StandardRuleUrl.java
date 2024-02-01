package org.jeecg.modules.et.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 平台标准化页面名称规则
 * @Author: jeecg-boot
 * @Date:   2024-01-31
 * @Version: V1.0
 */
@Data
@TableName("standard_rule_url")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="standard_rule_url对象", description="平台标准化页面名称规则")
public class StandardRuleUrl implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**客户端类型*/
	@Excel(name = "客户端类型", width = 15)
    @ApiModelProperty(value = "客户端类型")
    private String platformType;
	/**语言*/
	@Excel(name = "语言", width = 15)
    @ApiModelProperty(value = "语言")
    private String lang;
	/**模块*/
	@Excel(name = "模块", width = 15)
    @ApiModelProperty(value = "模块")
    private String unit;
	/**子模块*/
	@Excel(name = "子模块", width = 15)
    @ApiModelProperty(value = "子模块")
    private String subUnit;
	/**访问页面备注名称*/
	@Excel(name = "访问页面备注名称", width = 15)
    @ApiModelProperty(value = "访问页面备注名称")
    private String pageName;
	/**原始URL*/
	@Excel(name = "原始URL", width = 15)
    @ApiModelProperty(value = "原始URL")
    private String scUrl;
    /**标准URL*/
    @Excel(name = "标准URL", width = 15)
    @ApiModelProperty(value = "标准URL")
    private String standardUrl;
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
}
