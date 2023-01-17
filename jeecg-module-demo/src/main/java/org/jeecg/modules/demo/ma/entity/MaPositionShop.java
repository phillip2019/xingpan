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
 * @Description: 点位店铺
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
@Data
@TableName("ma_position_shop")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ma_position_shop对象", description="点位店铺")
public class MaPositionShop implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键编号*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键编号")
    private Long id;
	/**点位ID*/
	@Excel(name = "点位ID", width = 15, dictTable = "ma_position", dicText = "id", dicCode = "id")
	@Dict(dictTable = "ma_position", dicText = "id", dicCode = "id")
    @ApiModelProperty(value = "点位ID")
    private Long positionId;
	/**点位编号*/
	@Excel(name = "点位编号", width = 15, dictTable = "ma_position", dicText = "position_no", dicCode = "position_no")
	@Dict(dictTable = "ma_position", dicText = "position_no", dicCode = "position_no")
    @ApiModelProperty(value = "点位编号")
    private String positionNo;
	/**店铺编号*/
	@Excel(name = "店铺编号", width = 15)
    @ApiModelProperty(value = "店铺编号")
    private String shopId;
	/**店铺名称*/
	@Excel(name = "店铺名称", width = 15)
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
	/**店铺主图链接*/
	@Excel(name = "店铺主图链接", width = 100)
    @ApiModelProperty(value = "店铺主图链接")
    private String shopCover;
	/**店铺主营商品数组*/
	@Excel(name = "店铺主营商品数组", width = 100)
    @ApiModelProperty(value = "店铺主营商品数组")
    private String mainGoodsArr;
    /**主营内容*/
    @Excel(name = "主营内容", width = 15)
    @ApiModelProperty(value = "主营内容")
    private String mainBusinessScope;
	/**店铺位置*/
	@Excel(name = "店铺位置", width = 15)
    @ApiModelProperty(value = "店铺位置")
    private String shopLocation;
	/**店铺介绍*/
	@Excel(name = "店铺介绍", width = 15)
    @ApiModelProperty(value = "店铺介绍")
    private String shopIntroduction;
	/**点位店铺微信二维码ticket*/
	@Excel(name = "点位店铺微信二维码ticket", width = 15)
    @ApiModelProperty(value = "点位店铺微信二维码ticket")
    private String qrCodeTicket;
	/**点位店铺微信二维码链接*/
	@Excel(name = "点位店铺微信二维码链接", width = 15)
    @ApiModelProperty(value = "点位店铺微信二维码链接")
    private String qrCodeUrl;
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
}
