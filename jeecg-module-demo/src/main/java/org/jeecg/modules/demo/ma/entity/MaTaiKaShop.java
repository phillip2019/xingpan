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
 * @Description: ma_tai_ka_shop
 * @Author: jeecg-boot
 * @Date:   2023-04-03
 * @Version: V1.0
 */
@Data
@TableName("ma_tai_ka_shop")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ma_tai_ka_shop对象", description="ma_tai_ka_shop")
public class MaTaiKaShop implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
    @ApiModelProperty(value = "id")
    private Integer id;
	/**活动编号*/
	@Excel(name = "活动编号", width = 15)
    @ApiModelProperty(value = "活动编号")
    private Integer activeId;
	/**店铺所属市场*/
	@Excel(name = "店铺所属市场", width = 15)
    @ApiModelProperty(value = "店铺所属市场")
    private String marketName;
	/**店铺编号*/
	@Excel(name = "店铺编号", width = 15)
    @ApiModelProperty(value = "店铺编号")
    private String shopId;
	/**店铺名称*/
	@Excel(name = "店铺名称", width = 15)
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
	/**商铺编号数组，逗号分隔*/
	@Excel(name = "商铺编号数组，逗号分隔", width = 15)
    @ApiModelProperty(value = "商铺编号数组，逗号分隔")
    private String boothIdArr;
	/**商铺号数组，逗号分隔*/
	@Excel(name = "商铺号数组，逗号分隔", width = 15)
    @ApiModelProperty(value = "商铺号数组，逗号分隔")
    private String boothNoArr;
	/**店铺台卡微信二维码ticket*/
	@Excel(name = "店铺台卡微信二维码ticket", width = 15)
    @ApiModelProperty(value = "店铺台卡微信二维码ticket")
    private String qrCodeTicket;
	/**店铺台卡微信二维码链接*/
	@Excel(name = "店铺台卡微信二维码链接", width = 15)
    @ApiModelProperty(value = "店铺台卡微信二维码链接")
    private String qrCodeUrl;
	/**微信公众号带参数二维码实际指向地址*/
	@Excel(name = "微信公众号带参数二维码实际指向地址", width = 15)
    @ApiModelProperty(value = "微信公众号带参数二维码实际指向地址")
    private String url;
	/**台卡状态*/
	@Excel(name = "台卡状态", width = 15)
    @ApiModelProperty(value = "台卡状态")
    private Integer status;
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
