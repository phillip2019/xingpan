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
 * @Description: et_bu_project
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Data
@TableName("et_bu_project")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="et_bu_project对象", description="et_bu_project")
public class EtBuProject implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID，自增*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID，自增")
    private java.lang.String id;
	/**项目中文名称*/
	@Excel(name = "项目中文名称", width = 15)
    @ApiModelProperty(value = "项目中文名称")
    private java.lang.String name;
	/**项目描述*/
	@Excel(name = "项目描述", width = 15)
    @ApiModelProperty(value = "项目描述")
    private java.lang.String projectDesc;
	/**产品经理*/
	@Excel(name = "产品经理", width = 15)
    @ApiModelProperty(value = "产品经理")
    private java.lang.String productManager;
	/**产品原型链接*/
	@Excel(name = "产品原型链接", width = 15)
    @ApiModelProperty(value = "产品原型链接")
    private java.lang.String prototypeLink;
	/**上线时间*/
	@Excel(name = "上线时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上线时间")
    private java.util.Date onlineTime;
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
