package org.jeecg.modules.demo.ma.entity;

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
    private Integer id;
	/**点位编号*/
	@Excel(name = "点位编号", width = 15)
    @ApiModelProperty(value = "点位编号")
    private String positionNo;
	/**点位类型*/
	@Excel(name = "点位类型", width = 15)
    @ApiModelProperty(value = "点位类型")
    private String positionType;
	/**点位序号*/
	@Excel(name = "点位序号", width = 15)
    @ApiModelProperty(value = "点位序号")
    private String seqNo;
	/**活动编号*/
	@Excel(name = "活动编号", width = 15)
    @ApiModelProperty(value = "活动编号")
    private Integer activeId;
	/**点位管理员账号*/
	@Excel(name = "点位管理员账号", width = 15)
    @ApiModelProperty(value = "点位管理员账号")
    private String ownerAccount;
	/**点位管理员姓名*/
	@Excel(name = "点位管理员姓名", width = 15)
    @ApiModelProperty(value = "点位管理员姓名")
    private String ownerName;
	/**点位微信二维码ticket*/
	@Excel(name = "点位微信二维码ticket", width = 15)
    @ApiModelProperty(value = "点位微信二维码ticket")
    private String qrCodeTicket;
	/**点位微信二维码链接*/
	@Excel(name = "点位微信二维码链接", width = 15)
    @ApiModelProperty(value = "点位微信二维码链接")
    private String qrCodeUrl;
	/**点位状态*/
	@Excel(name = "点位状态", width = 15)
    @ApiModelProperty(value = "点位状态")
    private String status;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
