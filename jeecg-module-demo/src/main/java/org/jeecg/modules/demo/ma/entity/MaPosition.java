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
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 活动点位
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
@Data
@TableName("ma_position")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ma_position对象", description="活动点位")
public class MaPosition implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键编号*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键编号")
    private Long id;
	/**点位编号*/
	@Excel(name = "点位编号", width = 15)
    @ApiModelProperty(value = "点位编号")
    private String positionNo;
	/**点位类型*/
	@Excel(name = "点位类型", width = 15)
    @ApiModelProperty(value = "点位类型")
    private String positionType;
	/**点位序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private String seqNo;
	/**活动编号*/
	@Excel(name = "活动编号", width = 15)
    @ApiModelProperty(value = "活动编号")
    private Long activeId;
    /**市场名称*/
    @Excel(name = "市场", width = 15)
    @ApiModelProperty(value = "市场")
    private String marketName;
    /**楼层*/
    @Excel(name = "楼层", width = 15)
    @ApiModelProperty(value = "楼层")
    private String floor;
	/**点位管理员账号*/
	@Excel(name = "管理员账号", width = 15)
    @ApiModelProperty(value = "管理员账号")
    private String ownerAccount;
	/**点位管理员姓名*/
	@Excel(name = "管理员名字", width = 15)
    @ApiModelProperty(value = "管理员名字")
    private String ownerName;
	/**微信二维码ticket*/
	@Excel(name = "微信二维码ticket", width = 15)
    @ApiModelProperty(value = "微信二维码ticket")
    private String qrCodeTicket;
	/**微信二维码链接*/
	@Excel(name = "微信二维码链接", width = 15)
    @ApiModelProperty(value = "微信二维码链接")
    private String qrCodeUrl;
    /**公众号带参数二维码链接*/
    @Excel(name = "公众号带参数二维码链接", width = 15)
    @ApiModelProperty(value = "公众号带参数二维码链接")
    private String url;
	/**点位状态*/
	@Excel(name = "点位状态", width = 15)
    @ApiModelProperty(value = "点位状态")
    private Integer status;
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

    /**易拉宝上店铺信息*/
    @TableField(exist = false)
    @JsonIgnore
	private transient List<MaPositionShop> positionShopList = new ArrayList<>(4);

    /**易拉宝所属地址信息*/
    @TableField(exist = false)
    @JsonIgnore
    private transient MaPositionAddress positionAddress;
}
