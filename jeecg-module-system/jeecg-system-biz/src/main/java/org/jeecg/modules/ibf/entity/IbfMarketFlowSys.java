package org.jeecg.modules.ibf.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
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

import static org.jeecg.modules.ibf.IbfConst.HUNDRED;

/**
 * @Description: ibf_market_flow_sys
 * @Author: jeecg-boot
 * @Date:   2025-01-17
 * @Version: V1.0
 */
@Data
@TableName("ibf_market_flow_sys")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ibf_market_flow_sys对象", description="ibf_market_flow_sys")
public class IbfMarketFlowSys implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**缩写市场ID*/
	@Excel(name = "缩写市场ID", width = 15)
    @ApiModelProperty(value = "缩写市场ID")
    private String shortMarketId;
	/**日期 yyyy-MM-dd*/
	@Excel(name = "日期 yyyy-MM-dd", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "日期 yyyy-MM-dd")
    private Date dateCol;
	/**日人流量*/
	@Excel(name = "日人流量", width = 15)
    @ApiModelProperty(value = "日人流量")
    private Integer marketBuyerEntrNum1d;
	/**日车流量*/
	@Excel(name = "日车流量", width = 15)
    @ApiModelProperty(value = "日车流量")
    private Integer carEntrNum1d;
	/**日开门率*/
	@Excel(name = "日开门率", width = 15)
    @ApiModelProperty(value = "日开门率")
    private BigDecimal boothOpeningRate1d;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    public void customDB2VO() {
        if (this.boothOpeningRate1d != null) {
            this.boothOpeningRate1d = this.boothOpeningRate1d.multiply(HUNDRED);
        }
    }
    public void customVO2DB() {
        if (this.boothOpeningRate1d != null) {
            this.boothOpeningRate1d = this.boothOpeningRate1d.divide(HUNDRED, 2, RoundingMode.HALF_UP);
        }
    }
}
