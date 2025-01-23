package org.jeecg.modules.ibf.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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

import static org.jeecg.modules.ibf.IbfConst.HUNDRED;

/**
 * @Description: 月市场资源-市场流量
 * @Author: jeecg-boot
 * @Date:   2025-01-15
 * @Version: V1.0
 */
@Data
@TableName("ibf_market_resource_flow")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ibf_market_resource_flow对象", description="月市场资源-市场流量")
public class IbfMarketResourceFlow extends IbfCommonEntity implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**市场名称*/
    @ApiModelProperty(value = "市场名称")
    private String shortMarketName;
	/**人流（人次）*/
	@Excel(name = "人流", width = 8, type=4)
    @ApiModelProperty(value = "人流（人次）")
    @TableField("market_buyer_entr_num_1m")
    private BigDecimal marketBuyerEntrNum1m;
	/**车流（人次）*/
	@Excel(name = "车流", width = 8, type=4)
    @ApiModelProperty(value = "车流（人次）")
    @TableField("car_entr_num_1m")
    private BigDecimal carEntrNum1m;
	/**外商（人次）*/
	@Excel(name = "外商", width = 15)
    @ApiModelProperty(value = "外商（人次）")
    @TableField("foreign_buyer_entr_num_1m")
    private BigDecimal foreignBuyerEntrNum1m;
	/**开门率 %*/
	@Excel(name = "开门率", width = 8, type = 4)
    @ApiModelProperty(value = "开门率")
    @TableField("booth_opening_rate_1m")
    private BigDecimal boothOpeningRate1m;
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


    @Override
    public void customDB2VO() {
        super.customDB2VO();
        if (this.boothOpeningRate1m != null) {
            this.boothOpeningRate1m = this.boothOpeningRate1m.multiply(HUNDRED);
        }
    }

    @Override
    public void customVO2DB() {
        super.customVO2DB();

        if (this.boothOpeningRate1m != null) {
            this.boothOpeningRate1m = this.boothOpeningRate1m.divide(HUNDRED, 2, RoundingMode.HALF_UP);
        }
    }
}
