package org.jeecg.modules.mkt.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

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
 * @Description: 营销渠道链接
 * @Author: jeecg-boot
 * @Date:   2023-12-04
 * @Version: V1.0
 */
@Data
@TableName("mkt_channel_link")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="mkt_channel_link对象", description="营销渠道链接")
public class MktChannelLink implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**PC端落地页*/
	@Excel(name = "PC端落地页", width = 15)
    @ApiModelProperty(value = "PC端落地页")
    private String pcSourceUrl;
    /**移动端落地页*/
    @Excel(name = "移动端落地页", width = 15)
    @ApiModelProperty(value = "移动端落地页")
    private String wapSourceUrl;
	/**状态*/
    @Dict(dicCode = "CHANNEL_LINK_STATUS")
	@Excel(name = "状态", width = 15, dicCode = "CHANNEL_LINK_STATUS")
    @ApiModelProperty(value = "状态")
    private Integer status;
	/**活动名称*/
	@Excel(name = "活动名称", width = 15)
    @ApiModelProperty(value = "活动名称")
    private String utmCampaign;
	/**广告来源*/
	@Excel(name = "广告来源", width = 15)
    @ApiModelProperty(value = "广告来源")
    private String utmSource;
	/**广告媒介*/
	@Excel(name = "广告媒介", width = 15)
    @ApiModelProperty(value = "广告媒介")
    private String utmMedium;
	/**关键字*/
	@Excel(name = "关键字", width = 15)
    @ApiModelProperty(value = "关键字")
    private String utmTerm;
	/**广告内容*/
	@Excel(name = "广告内容", width = 15)
    @ApiModelProperty(value = "广告内容")
    private String utmContent;
	/**PC端带参链接*/
	@Excel(name = "PC端带参链接", width = 15)
    @ApiModelProperty(value = "PC端带参链接")
    private String pcTargetUrl;
    /**移动端带参链接*/
    @Excel(name = "移动端带参链接", width = 15)
    @ApiModelProperty(value = "移动端带参链接")
    private String wapTargetUrl;
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

    /**ids*/
    @ApiModelProperty(value = "ids")
    @TableField(exist = false)
    private List<String> ids;
}
