package org.jeecg.modules.cg.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 部门指标值
 * @Author: jeecg-boot
 * @Date:   2023-02-14
 * @Version: V1.0
 */
@Data
@TableName("cg_dept_index_value")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cg_dept_index_value对象", description="部门指标值")
public class CgDeptIndexValue implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**部门指标编号*/
	@Excel(name = "部门指标编号", width = 15)
    @ApiModelProperty(value = "部门指标编号")
    private java.lang.String deptIndexId;
    /**部门编号*/
    @Excel(name = "部门编号", width = 15)
    @ApiModelProperty(value = "部门编号")
    @TableField(exist = false)
    private transient String deptId;
    /**部门名称*/
    @Excel(name = "部门名称", width = 15)
    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    private transient String deptText;
    /**指标名称中文名*/
    @Excel(name = "指标名称中文名", width = 15)
    @ApiModelProperty(value = "指标名称中文名")
    @TableField(exist = false)
    private transient String indexNameZh;
	/**部门值*/
	@Excel(name = "部门值", width = 15)
    @ApiModelProperty(value = "部门值")
    private java.math.BigDecimal indexValue;
	/**开始日期*/
	@Excel(name = "开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
    private java.util.Date beginDate;
	/**结束日期*/
	@Excel(name = "结束日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束日期")
    private java.util.Date endDate;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
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
