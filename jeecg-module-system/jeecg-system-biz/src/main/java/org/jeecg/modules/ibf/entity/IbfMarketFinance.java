package org.jeecg.modules.ibf.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.constraints.Pattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import me.zhyd.oauth.utils.StringUtils;
import org.jeecg.modules.system.util.ValidMonthFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 业财一体-财务填报
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
@Data
@TableName("ibf_market_finance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ibf_market_finance对象", description="财务看板(BOSS)-每月填报")
public class IbfMarketFinance implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID主键")
    private String id;
	/**业务类型*/
    @ApiModelProperty(value = "业务类型")
    private String businessVersion;
	/**市场*/
    @Dict(dicCode = "finance_short_market_id")
    @Excel(name = "市场", width = 15, dicCode = "finance_short_market_id")
    @ApiModelProperty(value = "市场ID")
    private String shortMarketId;
	/**所属年月 yyyy-MM*/
	@Excel(name = "月份", width = 15, databaseFormat="yyyy-MM", importFormat="yyyy-MM", importConvert = true)
    @ApiModelProperty(value = "所属年月 yyyy-MM")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM")
    @DateTimeFormat(pattern="yyyy-MM")
    private String monthCol;
	/**本期收入(万)*/
	@Excel(name = "本期收入", width = 15)
    @ApiModelProperty(value = "本期收入(万)")
    @TableField("cur_period_income_1m")
    private BigDecimal curPeriodIncome1m;
	/**本期营收(万)*/
	@Excel(name = "本期营收", width = 15)
    @ApiModelProperty(value = "本期营收(万)")
    private BigDecimal turnoverIncomeSd;
	/**目标营收(万)*/
	@Excel(name = "目标营收", width = 15)
    @ApiModelProperty(value = "目标营收")
    private BigDecimal targetTurnoverIncomeSd;
	/**本期利润(万)*/
	@Excel(name = "本期利润", width = 15)
    @ApiModelProperty(value = "本期利润(万)")
    private BigDecimal accumulateProfitIncomeSd;
	/**目标利润(万)*/
	@Excel(name = "目标利润", width = 15)
    @ApiModelProperty(value = "目标利润(万)")
    private BigDecimal targetProfitIncomeSd;
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

    public String convertsetMonthCol(String text) {
        // 若text为空，则直接报错
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("月份格式不可以为空");
        }
        // 若是YYYY-MM-ddTHH:mm:ss格式，则转换为yyyy-MM
        if (text.contains("T")) {
            text = text.substring(0, 7);
        }
        return text;
    }
}
