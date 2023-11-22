package org.jeecg.modules.et.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 客户端事件表截图
 * @Author: jeecg-boot
 * @Date:   2023-11-13
 * @Version: V1.0
 */
@Data
@TableName("et_client_event_screenshot")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="et_client_event_screenshot对象", description="客户端事件表截图")
public class EtClientEventScreenshot implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID，自增*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID，自增")
    private java.lang.String id;
    /**事件ID*/
    @Excel(name = "事件ID", width = 15)
    @ApiModelProperty(value = "事件ID")
    @TableField(exist = false)
    private java.lang.String eventId;
    /**客户端ID*/
    @Excel(name = "客户端ID", width = 15)
    @ApiModelProperty(value = "客户端ID")
    @TableField(exist = false)
    private java.lang.String clientId;
	/**事件客户端编号，关联et_client_event表的id*/
	@Excel(name = "事件客户端编号，关联et_client_event表的id", width = 15)
    @ApiModelProperty(value = "事件客户端编号，关联et_client_event表的id")
    private java.lang.String clientEventId;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
	/**模块名称*/
	@Excel(name = "模块名称", width = 15)
    @ApiModelProperty(value = "模块名称")
    private java.lang.String unitName;
	/**页面名称*/
	@Excel(name = "页面名称", width = 15)
    @ApiModelProperty(value = "页面名称")
    private java.lang.String pageName;
	/**埋点位置*/
	@Excel(name = "埋点位置", width = 15)
    @ApiModelProperty(value = "埋点位置")
    private java.lang.String pagePosition;
	/**图片存储路径*/
	@Excel(name = "图片存储路径", width = 15)
    @ApiModelProperty(value = "图片存储路径")
    private java.lang.String screenshot;
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
