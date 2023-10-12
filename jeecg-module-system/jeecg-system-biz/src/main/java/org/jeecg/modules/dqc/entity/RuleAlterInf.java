package org.jeecg.modules.dqc.entity;

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
 * @Description: 数据质量预警规则
 * @Author: jeecg-boot
 * @Date:   2023-10-12
 * @Version: V1.0
 */
@Data
@TableName("rule_alter_inf")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="rule_alter_inf对象", description="数据质量预警规则")
public class RuleAlterInf implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**JOB名称*/
	@Excel(name = "JOB名称", width = 15)
    @ApiModelProperty(value = "JOB名称")
    private String jobName;
	/**度量名称*/
	@Excel(name = "度量名称", width = 15)
    @ApiModelProperty(value = "度量名称")
    private String measureName;
	/**度量描述*/
	@Excel(name = "度量描述", width = 15)
    @ApiModelProperty(value = "度量描述")
    private String measureDesc;
	/**定时表达式*/
	@Excel(name = "定时表达式", width = 15)
    @ApiModelProperty(value = "定时表达式")
    private String cronExpression;
	/**SPARK任务ID*/
	@Excel(name = "SPARK任务ID", width = 15)
    @ApiModelProperty(value = "SPARK任务ID")
    private String applicationId;
	/**执行日期*/
	@Excel(name = "执行日期", width = 15)
    @ApiModelProperty(value = "执行日期")
    private String executeDate;
	/**JOB创建者*/
	@Excel(name = "JOB创建者", width = 15)
    @ApiModelProperty(value = "JOB创建者")
    private String jobOwer;
	/**DQ类型*/
	@Excel(name = "DQ类型", width = 15)
    @ApiModelProperty(value = "DQ类型")
    private String dqType;
	/**指标描述*/
	@Excel(name = "指标描述", width = 15)
    @ApiModelProperty(value = "指标描述")
    private String fieldDesc;
	/**最小值*/
	@Excel(name = "最小值", width = 15)
    @ApiModelProperty(value = "最小值")
    private String startValue;
	/**表达式1*/
	@Excel(name = "表达式1", width = 15)
    @ApiModelProperty(value = "表达式1")
    private String compareExpression1;
	/**表达式2*/
	@Excel(name = "表达式2", width = 15)
    @ApiModelProperty(value = "表达式2")
    private String compareExpression2;
	/**最大值*/
	@Excel(name = "最大值", width = 15)
    @ApiModelProperty(value = "最大值")
    private String endValue;
	/**规则*/
	@Excel(name = "规则", width = 15)
    @ApiModelProperty(value = "规则")
    private String rule;
	/**连接类型*/
	@Excel(name = "连接类型", width = 15)
    @ApiModelProperty(value = "连接类型")
    private String connType;
	/**指标名称*/
	@Excel(name = "指标名称", width = 15)
    @ApiModelProperty(value = "指标名称")
    private String fieldName;
	/**源表名称*/
	@Excel(name = "源表名称", width = 15)
    @ApiModelProperty(value = "源表名称")
    private String tableNames;
	/**指标值*/
	@Excel(name = "指标值", width = 15)
    @ApiModelProperty(value = "指标值")
    private String value;
	/**0生效 1失效*/
	@Excel(name = "0生效 1失效", width = 15)
    @ApiModelProperty(value = "0生效 1失效")
    private String status;
	/**是否异常0正常 1异常*/
	@Excel(name = "是否异常0正常 1异常", width = 15)
    @ApiModelProperty(value = "是否异常0正常 1异常")
    private String isAbnormal;
	/**校验强度 A强/B中/C弱*/
	@Excel(name = "校验强度 A强/B中/C弱", width = 15)
    @ApiModelProperty(value = "校验强度 A强/B中/C弱")
    private String ruleType;
	/**负责人*/
	@Excel(name = "负责人", width = 15)
    @ApiModelProperty(value = "负责人")
    private String ownerName;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createdAt;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updatedAt;
}
