package org.jeecg.modules.ibf.entity;

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
 * @Description: 填报发布汇总
 * @Author: jeecg-boot
 * @Date:   2025-01-17
 * @Version: V1.0
 */
@Data
@TableName("ibf_reporting_summary")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ibf_reporting_summary对象", description="填报发布汇总")
public class IbfReportingSummary implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private Integer id;
	/**年份*/
	@Excel(name = "年份", width = 15)
    @ApiModelProperty(value = "年份")
    private Integer year;
	/**市场ID缩写*/
	@Excel(name = "市场ID缩写", width = 15)
    @ApiModelProperty(value = "市场ID缩写")
    private String shortMarketId;
	/**月份*/
	@Excel(name = "月份", width = 15)
    @ApiModelProperty(value = "月份")
    private Integer month;
	/**统计开始日期*/
	@Excel(name = "统计开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "统计开始日期")
    private Date statStartDate;
	/**统计结束日期*/
	@Excel(name = "统计结束日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "统计结束日期")
    private Date statEndDate;
	/**资源-资源填报ID*/
	@Excel(name = "资源-资源填报ID", width = 15)
    @ApiModelProperty(value = "资源-资源填报ID")
    private Integer resourceId;
	/**资源-GMV填报ID*/
	@Excel(name = "资源-GMV填报ID", width = 15)
    @ApiModelProperty(value = "资源-GMV填报ID")
    private Integer resourceGmvId;
	/**资源-流量填报ID*/
	@Excel(name = "资源-流量填报ID", width = 15)
    @ApiModelProperty(value = "资源-流量填报ID")
    private Integer resourceTrafficId;
	/**财务填报ID*/
	@Excel(name = "财务填报ID", width = 15)
    @ApiModelProperty(value = "财务填报ID")
    private Integer financeId;
	/**每日流量填报ID*/
	@Excel(name = "每日流量填报ID", width = 15)
    @ApiModelProperty(value = "每日流量填报ID")
    private Integer flowId;
	/**发布状态: 0 校准中, 1 已发布*/
	@Excel(name = "发布状态: 0 校准中, 1 已发布", width = 15)
    @ApiModelProperty(value = "发布状态: 0 校准中, 1 已发布")
    private Integer isPublish;
	/**复制状态: 0 未复制, 1 已复制*/
	@Excel(name = "复制状态: 0 未复制, 1 已复制", width = 15)
    @ApiModelProperty(value = "复制状态: 0 未复制, 1 已复制")
    private Integer isCopy;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**创建者*/
    @ApiModelProperty(value = "创建者")
    private String createBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**更新者*/
    @ApiModelProperty(value = "更新者")
    private String updateBy;
	/**删除状态: 0 正常, 1 删除*/
	@Excel(name = "删除状态: 0 正常, 1 删除", width = 15)
    @ApiModelProperty(value = "删除状态: 0 正常, 1 删除")
    private Integer isDeleted;
}
