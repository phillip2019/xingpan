package org.jeecg.modules.et.entity;

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
 * @Description: et_event_property
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Data
@TableName("et_event_property")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="et_event_property对象", description="et_event_property")
public class EtEventProperty implements Serializable {
    private static final long serialVersionUID = 1L;

	/**事件属性表主键,自增Id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "事件属性表主键,自增Id")
    private java.lang.Integer id;
	/**所属事件的Id*/
	@Excel(name = "所属事件的Id", width = 15)
    @ApiModelProperty(value = "所属事件的Id")
    private java.lang.Integer eventId;
	/**属性的英文名称*/
	@Excel(name = "属性的英文名称", width = 15)
    @ApiModelProperty(value = "属性的英文名称")
    private java.lang.String name;
	/**属性的中文名称*/
	@Excel(name = "属性的中文名称", width = 15)
    @ApiModelProperty(value = "属性的中文名称")
    private java.lang.String zhName;
	/**属性值类型,1-字符串,2-数值,3-BOOL,4-列表*/
	@Excel(name = "属性值类型,1-字符串,2-数值,3-BOOL,4-列表", width = 15)
    @ApiModelProperty(value = "属性值类型,1-字符串,2-数值,3-BOOL,4-列表")
    private java.lang.Integer type;
	/**属性值示例*/
	@Excel(name = "属性值示例", width = 15)
    @ApiModelProperty(value = "属性值示例")
    private java.lang.String example;
	/**对属性的说明描述*/
	@Excel(name = "对属性的说明描述", width = 15)
    @ApiModelProperty(value = "对属性的说明描述")
    private java.lang.String propertyDesc;
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
