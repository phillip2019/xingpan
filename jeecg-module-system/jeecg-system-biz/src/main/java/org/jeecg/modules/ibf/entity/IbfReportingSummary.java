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
import org.jeecg.common.util.DateUtil;
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
@ApiModel(value="ibf_reporting_summary对象", description="大屏发布")
public class IbfReportingSummary implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**所属年月 yyyy-MM*/
    @Excel(name = "月份", width = 8, importConvert = true, type = 4)
    @ApiModelProperty(value = "所属年月")
    private String monthCol;

	/**年份*/
	@Excel(name = "年份", width = 15)
    @ApiModelProperty(value = "年份")
    private Integer year;
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
	/**复制状态: 0 未复制, 1 已复制*/
    @ApiModelProperty(value = "复制状态: 0 未复制, 1 已复制")
    private Integer isCopy;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;

    /**是否发布*/
    @Dict(dicCode = "is_publish")
    @ApiModelProperty(value = "是否发布")
    private Integer isPublish;

    /**
     * 当前月份标记
     * 0: 当前月份
     * 1: 前1个月
     * */
    @ApiModelProperty(value = "当前月份标记")
    private Integer flag;

    /**
     * 当前版本,是否可见
     * 0: 不可见
     * 1: 可见
     * */
    @Dict(dicCode = "is_visible")
    @ApiModelProperty(value = "是否可见")
    private Integer isVisible;

    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**修改时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**修改人*/
    @ApiModelProperty(value = "修改人")
    private String updateBy;
	/**删除状态*/
    @ApiModelProperty(value = "删除状态: 0 正常, 1 删除")
    private Integer isDeleted;

    public void convertsetMonthCol(String text) {
        this.monthCol = DateUtil.convertMonthCol(text);
    }

}
