package org.jeecg.modules.ma.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 商铺台卡店铺二维码
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ma_active_tai_ka_qr_code_url对象", description="台卡店铺二维图片地址对象")
public class MaActiveTaiKaQrCodeUrl implements Serializable {
    private static final long serialVersionUID = 1L;

	/**市场名称*/
	@Excel(name = "市场", width = 15)
    @ApiModelProperty(value = "市场")
    private String marketName;
    /**店铺ID*/
    @Excel(name = "店铺ID", width = 15)
    @ApiModelProperty(value = "店铺ID")
    private String shopId;
    /**店铺名称*/
    @Excel(name = "店铺名称", width = 15)
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**台卡店铺二维码地址*/
    @Excel(name = "台卡店铺二维码地址", width = 100)
    @ApiModelProperty(value = "台卡店铺二维码地址")
    private String taiKaShopQrCodeUrl;
}
