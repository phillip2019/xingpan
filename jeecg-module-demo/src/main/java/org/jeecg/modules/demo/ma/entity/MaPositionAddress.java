package org.jeecg.modules.demo.ma.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 点位位置
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
@Data
@TableName("ma_position_address")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ma_position_address对象", description="点位位置")
public class MaPositionAddress implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键编号*/
	@TableId()
    @ApiModelProperty(value = "主键编号")
    private String id;
	/**点位ID*/
	@Excel(name = "点位ID", width = 15)
    @ApiModelProperty(value = "点位ID")
    private String positionId;
	/**点位编号*/
	@Excel(name = "点位编号", width = 15)
    @ApiModelProperty(value = "点位编号")
    private String positionNo;
	/**市场名称*/
	@Excel(name = "市场名称", width = 15)
    @ApiModelProperty(value = "市场名称")
    private String marketName;
	/**市场楼层*/
	@Excel(name = "市场楼层", width = 15)
    @ApiModelProperty(value = "市场楼层")
    private String floor;
	/**点位位置类型*/
	@Excel(name = "点位位置类型", width = 15)
    @ApiModelProperty(value = "点位位置类型")
    private String positionAddressType;
	/**点位详细位置*/
	@Excel(name = "点位详细位置", width = 15)
    @ApiModelProperty(value = "点位详细位置")
    private String positionAddressDetail;
	/**市场行业*/
	@Excel(name = "市场行业", width = 15)
    @ApiModelProperty(value = "市场行业")
    private String industryName;
	/**点位位置状态*/
	@Excel(name = "点位位置状态", width = 15)
    @ApiModelProperty(value = "点位位置状态")
    private Integer usedStatus;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    /**点位序号*/
    @Excel(name = "点位序号", width = 15)
    @ApiModelProperty(value = "点位序号")
    @TableField(exist = false)
    private String positionSeqNo;
}
