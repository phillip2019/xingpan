package org.jeecg.modules.ibf.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import me.zhyd.oauth.utils.StringUtils;
import org.jeecg.common.util.DateUtil;
import org.jeecg.common.util.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
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
public class IbfMarketFlow implements Serializable {
    private static final long serialVersionUID = 1L;

	/**业财一体-每日填报市场流量*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID，自增主键，无实际含义")
    private String id;
    /**业务类型*/
    @ApiModelProperty(value = "业务类型")
    private String businessVersion;
	/**市场ID*/
    @Dict(dicCode = "short_market_id")
	@Excel(name = "市场", width = 15, dicCode = "short_market_id")
    @ApiModelProperty(value = "市场ID")
    private java.lang.String shortMarketId;
	/**日期 yyyy-MM-dd*/
	@Excel(name = "日期", width = 15, format = "yyyy-MM-dd", importFormat = "yyyy-MM-dd", importConvert = true)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "日期 yyyy-MM-dd")
    private Date dateCol;
	/**日人流量*/
	@Excel(name = "日人流量", width = 15)
    @ApiModelProperty(value = "日人流量")
    @TableField("market_buyer_entr_num_1d")
    private Integer marketBuyerEntrNum1d;
	/**日车流量*/
	@Excel(name = "日车流量", width = 15)
    @ApiModelProperty(value = "日车流量")
    @TableField("car_entr_num_1d")
    private Integer carEntrNum1d;
	/**日开门率*/
	@Excel(name = "日开门率", width = 15)
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
        this.dateCol = DateUtils.parseDate(DateUtil.convertDateCol(text), "yyyy-MM-dd");
    }
}
