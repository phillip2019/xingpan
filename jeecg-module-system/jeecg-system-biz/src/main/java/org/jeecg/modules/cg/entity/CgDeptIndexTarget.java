package org.jeecg.modules.cg.entity;

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
 * @Description: cg_dept_index_target
 * @Author: jeecg-boot
 * @Date:   2023-02-19
 * @Version: V1.0
 */
@Data
@TableName("cg_dept_index_target")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cg_dept_index_target对象", description="cg_dept_index_target")
public class CgDeptIndexTarget implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**指标编号*/
	@Excel(name = "指标编号", width = 15)
    @ApiModelProperty(value = "指标编号")
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
	/**指标目标值*/
	@Excel(name = "指标目标值", width = 15)
    @ApiModelProperty(value = "指标目标值")
    private java.math.BigDecimal targetIndexValue;
	/**指标所属年*/
	@Excel(name = "指标所属年", width = 15)
    @ApiModelProperty(value = "指标所属年")
    private java.lang.Integer yearCol;
	/**指标所属季度*/
	@Excel(name = "指标所属季度", width = 15)
    @ApiModelProperty(value = "指标所属季度")
    private java.lang.Integer quarterCol;
	/**指标所属月份*/
	@Excel(name = "指标所属月份", width = 15)
    @ApiModelProperty(value = "指标所属月份")
    private java.lang.Integer monthCol;
	/**指标统计类型, sum|avg|max|min*/
	@Excel(name = "指标统计类型, sum|avg|max|min", width = 15)
    @ApiModelProperty(value = "指标统计类型, sum|avg|max|min")
    private java.lang.String indexStatsType;
	/**0: 弃用 1: 正常*/
	@Excel(name = "0: 弃用 1: 正常", width = 15)
    @ApiModelProperty(value = "0: 弃用 1: 正常")
    private java.lang.Integer status;
	/**目标备注*/
	@Excel(name = "目标备注", width = 15)
    @ApiModelProperty(value = "目标备注")
    private java.lang.String remark;
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
	/**0: 未删除, 1: 删除*/
	@Excel(name = "0: 未删除, 1: 删除", width = 15)
    @ApiModelProperty(value = "0: 未删除, 1: 删除")
    private java.lang.Integer isDeleted;
}
