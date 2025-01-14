package org.jeecg.modules.ibf.entity;

import java.io.Serializable;
import java.text.ParseException;
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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 业财一体-每日填报市场流量
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
@Data
@TableName("ibf_market_flow")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ibf_market_flow对象", description="业财一体-每日填报市场流量")
public class IbfMarketFlow extends IbfCommonEntity implements Serializable {
    private static final long serialVersionUID = 1L;

	/**业财一体-每日填报市场流量*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID，自增主键，无实际含义")
    private String id;
	/**日期 yyyy-MM-dd*/
	@Excel(name = "日期", width = 15, importConvert = true, type = 1)
    @ApiModelProperty(value = "日期 yyyy-MM-dd")
    private String dateCol;
	/**日人流量*/
	@Excel(name = "日人流量", width = 15, type = 4)
    @ApiModelProperty(value = "日人流量")
    @TableField("market_buyer_entr_num_1d")
    private Integer marketBuyerEntrNum1d;
	/**日车流量*/
	@Excel(name = "日车流量", width = 15, type = 4)
    @ApiModelProperty(value = "日车流量")
    @TableField("car_entr_num_1d")
    private Integer carEntrNum1d;
	/**日开门率*/
	@Excel(name = "日开门率", width = 15, type = 4)
    @ApiModelProperty(value = "日开门率")
    @TableField("booth_opening_rate_1d")
    private BigDecimal boothOpeningRate1d;
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

    public void convertsetDateCol(String text) throws ParseException {
        // 将日期格式yyyy-MM-dd格式转换为日期格式
        this.dateCol = DateUtil.convertDateCol(text);
    }
}
