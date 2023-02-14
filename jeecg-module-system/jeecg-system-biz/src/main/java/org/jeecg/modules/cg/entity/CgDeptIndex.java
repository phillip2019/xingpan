package org.jeecg.modules.cg.entity;

import java.io.Serializable;
import java.util.Date;

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
 * @Description: chinagoods平台部门指标清册
 * @Author: jeecg-boot
 * @Date:   2023-02-13
 * @Version: V1.0
 */
@Data
@TableName("cg_dept_index")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cg_dept_index对象", description="chinagoods平台部门指标清册")
public class CgDeptIndex implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
    @ApiModelProperty(value = "id")
    private Integer id;
	/**部门编号*/
	@Excel(name = "部门编号", width = 15)
    @ApiModelProperty(value = "部门编号")
    private String deptId;
    /**部门名称*/
    @Excel(name = "部门名称", width = 15)
    @ApiModelProperty(value = "部门名称")
    private String deptText;
	/**一级部门负责人*/
	@Excel(name = "一级部门负责人", width = 15)
    @ApiModelProperty(value = "一级部门负责人")
    private String firstLevelOwner;
	/**二级部门负责人*/
	@Excel(name = "二级部门负责人", width = 15)
    @ApiModelProperty(value = "二级部门负责人")
    private String secondLevelOwner;
	/**指标名称，英文名*/
	@Excel(name = "指标名称，英文名", width = 15)
    @ApiModelProperty(value = "指标名称，英文名")
    private String indexName;
	/**指标名称，中文名*/
	@Excel(name = "指标名称，中文名", width = 15)
    @ApiModelProperty(value = "指标名称，中文名")
    private String indexNameZh;
	/**指标释义*/
	@Excel(name = "指标释义", width = 15)
    @ApiModelProperty(value = "指标释义")
    private String indexInterpretation;
	/**指标周期, 累计|日|周|月|季度|年*/
	@Excel(name = "指标周期, 累计|日|周|月|季度|年", width = 15)
    @ApiModelProperty(value = "指标周期, 累计|日|周|月|季度|年")
    private String indicatorCycle;
	/**指标对接人*/
	@Excel(name = "指标对接人", width = 15)
    @ApiModelProperty(value = "指标对接人")
    private String indicatorDockingPerson;
	/**在周的第几天，数据填报日期*/
	@Excel(name = "在周的第几天，数据填报日期", width = 15)
    @ApiModelProperty(value = "在周的第几天，数据填报日期")
    private String weekDay;
	/**在天的小时填报开始*/
	@Excel(name = "在天的小时填报开始", width = 15)
    @ApiModelProperty(value = "在天的小时填报开始")
    private String dayHourBegin;
	/**在天的小时填报结束*/
	@Excel(name = "在天的小时填报结束", width = 15)
    @ApiModelProperty(value = "在天的小时填报结束")
    private String dayHourOver;
	/**活动备注*/
	@Excel(name = "活动备注", width = 15)
    @ApiModelProperty(value = "活动备注")
    private String remark;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
