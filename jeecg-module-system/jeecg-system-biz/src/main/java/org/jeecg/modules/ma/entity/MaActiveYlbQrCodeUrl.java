package org.jeecg.modules.ma.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 易拉宝二维码
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ma_active_ylb_qr_code_url对象", description="易拉宝二维图片地址对象")
public class MaActiveYlbQrCodeUrl implements Serializable {
    private static final long serialVersionUID = 1L;

	/**市场名称*/
	@Excel(name = "市场", width = 15)
    @ApiModelProperty(value = "市场")
    private String marketName;
    /**楼层*/
    @Excel(name = "楼层", width = 15)
    @ApiModelProperty(value = "楼层")
    private String floor;
    /**序号*/
    @Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private String seqNo;
    /**店铺ID*/
    @Excel(name = "店铺ID", width = 15)
    @ApiModelProperty(value = "店铺ID")
    private String shopId;
    /**店铺名称*/
    @Excel(name = "店铺名称", width = 15)
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**易拉宝二维码地址*/
    @Excel(name = "易拉宝二维码地址", width = 100)
    @ApiModelProperty(value = "易拉宝二维码地址")
    private String ylbQrCodeUrl;
    /**易拉宝店铺二维码地址*/
    @Excel(name = "易拉宝店铺二维码地址", width = 100)
    @ApiModelProperty(value = "易拉宝店铺二维码地址")
    private String ylbShopQrCodeUrl;
}
