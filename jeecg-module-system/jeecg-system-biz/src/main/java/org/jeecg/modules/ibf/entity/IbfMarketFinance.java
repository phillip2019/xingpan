package org.jeecg.modules.ibf.entity;

import java.io.Serializable;
import java.math.RoundingMode;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static org.jeecg.modules.ibf.IbfConst.TEN_THOUSAND;

/**
 * @Description: 业财一体-财务填报
 * @Author: jeecg-boot
 * @Date:   2024-12-19
 * @Version: V1.0
 */
@Slf4j
@Data
@TableName("ibf_market_finance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ibf_market_finance对象", description="财务看板(BOSS)-每月填报")
public class IbfMarketFinance extends IbfCommonEntity implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID主键")
    private String id;
	/**本期收入(万)*/
	@Excel(name = "本期收入", width = 15, type = 4, groupName = "营收")
    @ApiModelProperty(value = "本期收入(万)")
    @TableField("cur_period_income_1m")
    private BigDecimal curPeriodIncome1m;
	/**本期营收(万)*/
	@Excel(name = "本期营收", width = 15, type = 4, groupName = "营收")
    @ApiModelProperty(value = "本期营收(万)")
    @TableField("turnover_income_1m")
    private BigDecimal turnoverIncomeSd;
	/**目标营收(万)*/
	@Excel(name = "本年目标营收", width = 15, type = 4, groupName = "营收")
    @ApiModelProperty(value = "本年目标营收")
    private BigDecimal targetTurnoverIncomeSd;
	/**本期利润(万)*/
	@Excel(name = "本期利润", width = 15, type = 4, groupName = "利润")
    @ApiModelProperty(value = "本期利润(万)")
    @TableField("accumulate_profit_income_1m")
    private BigDecimal accumulateProfitIncomeSd;
	/**目标利润(万)*/
	@Excel(name = "本年目标利润", width = 15, type = 4, groupName = "利润")
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

    @Override
    public void customDB2VO() {
        if (this.curPeriodIncome1m != null) {
            this.curPeriodIncome1m = this.curPeriodIncome1m.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }
        if (this.turnoverIncomeSd != null) {
            this.turnoverIncomeSd = this.turnoverIncomeSd.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }
        if (this.targetTurnoverIncomeSd != null) {
            this.targetTurnoverIncomeSd = this.targetTurnoverIncomeSd.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }

        if (this.accumulateProfitIncomeSd != null) {
            this.accumulateProfitIncomeSd = this.accumulateProfitIncomeSd.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }
        if (this.targetProfitIncomeSd != null) {
            this.targetProfitIncomeSd = this.targetProfitIncomeSd.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }
    }

    @Override
    public void customVO2DB() {
        super.customVO2DB();
        if (this.curPeriodIncome1m != null) {
            this.curPeriodIncome1m = this.curPeriodIncome1m.multiply(TEN_THOUSAND);
        }
        if (this.turnoverIncomeSd != null) {
            this.turnoverIncomeSd = this.turnoverIncomeSd.multiply(TEN_THOUSAND);
        }
        if (this.targetTurnoverIncomeSd != null) {
            this.targetTurnoverIncomeSd = this.targetTurnoverIncomeSd.multiply(TEN_THOUSAND);
        }
        if (this.accumulateProfitIncomeSd != null) {
            this.accumulateProfitIncomeSd = this.accumulateProfitIncomeSd.multiply(TEN_THOUSAND);
        }
        if (this.targetProfitIncomeSd != null) {
            this.targetProfitIncomeSd = this.targetProfitIncomeSd.multiply(TEN_THOUSAND);
        }
    }
}
