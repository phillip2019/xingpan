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
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Table;

import static org.jeecg.modules.ibf.IbfConst.HUNDRED;
import static org.jeecg.modules.ibf.IbfConst.TEN_THOUSAND;

/**
 * @Description: ibf_market_resource_sys
 * @Author: jeecg-boot
 * @Date:   2025-01-17
 * @Version: V1.0
 */
@Data
@TableName("ibf_market_resource_sys")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ibf_market_resource_sys对象", description="ibf_market_resource_sys")
public class IbfMarketResourceSys implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**市场ID缩写*/
	@Excel(name = "市场ID缩写", width = 15)
    @ApiModelProperty(value = "市场ID缩写")
    private String shortMarketId;
    /**所属年月 yyyy-MM*/
    @Excel(name = "月份", width = 15)
    @ApiModelProperty(value = "月份")
    private String monthCol;
	/**市场名称*/
	@Excel(name = "市场名称", width = 15)
    @ApiModelProperty(value = "市场名称")
    private String shortMarketName;
	/**间数（商位）*/
	@Excel(name = "间数（商位）", width = 15)
    @ApiModelProperty(value = "间数（商位）")
    private BigDecimal boothRoomNumTd;
	/**间数（配套）*/
	@Excel(name = "间数（配套）", width = 15)
    @ApiModelProperty(value = "间数（配套）")
    private BigDecimal matchRoomNumTd;
	/**已出租间数（商位+配套）*/
	@Excel(name = "已出租间数（商位+配套）", width = 15)
    @ApiModelProperty(value = "已出租间数（商位+配套）")
    @TableField("booth_match_rent_room_num_1d")
    private BigDecimal boothMatchRentRoomNumTd;
    /**已出租间数（商位）*/
    @Excel(name = "已出租间数（商位）", width = 15)
    @ApiModelProperty(value = "已出租间数（商位）")
    @TableField("booth_rent_room_num_td")
    private BigDecimal boothRentRoomNumTd;
    /**已出租间数（配套）*/
    @Excel(name = "已出租间数（配套）", width = 15)
    @ApiModelProperty(value = "已出租间数（配套）")
    @TableField("match_rent_room_num_td")
    private BigDecimal matchRentRoomNumTd;
	/**面积（商位）㎡*/
	@Excel(name = "面积（商位）㎡", width = 15)
    @ApiModelProperty(value = "面积（商位）㎡")
    private BigDecimal boothAreaNumTd;
	/**面积（配套）㎡*/
	@Excel(name = "面积（配套）㎡", width = 15)
    @ApiModelProperty(value = "面积（配套）㎡")
    private BigDecimal matchAreaNumTd;
	/**已出租面积（商位+配套）㎡*/
	@Excel(name = "已出租面积（商位+配套）㎡", width = 15)
    @ApiModelProperty(value = "已出租面积（商位+配套）㎡")
    @TableField("booth_match_rent_area_num_1d")
    private BigDecimal boothMatchRentAreaNumTd;
    /**已出租面积（商位）㎡*/
    @Excel(name = "已出租面积（商位）㎡", width = 15)
    @ApiModelProperty(value = "已出租面积（商位）㎡")
    @TableField("booth_rent_area_num_td")
    private BigDecimal boothRentAreaNumTd;
    /**已出租面积（配套）㎡*/
    @Excel(name = "已出租面积（配套）㎡", width = 15)
    @ApiModelProperty(value = "已出租面积（配套）㎡")
    @TableField("match_rent_area_num_td")
    private BigDecimal matchRentAreaNumTd;
	/**资源情况统计日期 yyyy-MM-dd*/
	@Excel(name = "资源情况统计日期 yyyy-MM-dd", width = 15)
    @ApiModelProperty(value = "资源情况统计日期 yyyy-MM-dd")
    private String resourceStatisticsDate;
	/**商位使用权人*/
	@Excel(name = "商位使用权人", width = 15)
    @ApiModelProperty(value = "商位使用权人")
    private Integer boothOwnerNum;
	/**实际经营人*/
	@Excel(name = "实际经营人", width = 15)
    @ApiModelProperty(value = "实际经营人")
    private Integer boothOperatorNum;
	/**商人统计日期 yyyy-MM-dd*/
	@Excel(name = "商人统计日期 yyyy-MM-dd", width = 15)
    @ApiModelProperty(value = "商人统计日期 yyyy-MM-dd")
    private String merchantStatisticsDate;
	/**人流（人次）*/
	@Excel(name = "人流（人次）", width = 15)
    @ApiModelProperty(value = "人流（人次）")
    @TableField("market_buyer_entr_num_1m")
    private BigDecimal marketBuyerEntrNum1m;
	/**车流（人次）*/
	@Excel(name = "车流（人次）", width = 15)
    @ApiModelProperty(value = "车流（人次）")
    @TableField("car_entr_num_1m")
    private BigDecimal carEntrNum1m;
	/**外商（人次）*/
	@Excel(name = "外商（人次）", width = 15)
    @ApiModelProperty(value = "外商（人次）")
    @TableField("foreign_buyer_entr_num_1m")
    private BigDecimal foreignBuyerEntrNum1m;
	/**开门率 %*/
	@Excel(name = "开门率 %", width = 15)
    @ApiModelProperty(value = "开门率 %")
    @TableField("booth_opening_rate_1m")
    private BigDecimal boothOpeningRate1m;
	/**商位转让笔数*/
	@Excel(name = "商位转让笔数", width = 15)
    @ApiModelProperty(value = "商位转让笔数")
    @TableField("market_transfer_num_1m")
    private Integer marketTransferNum1m;
	/**商位转让均价（元）*/
	@Excel(name = "商位转让均价（元）", width = 15)
    @ApiModelProperty(value = "商位转让均价（元）")
    @TableField("market_transfer_price_avg_1m")
    private BigDecimal marketTransferPriceAvg1m;
	/**商位转租笔数*/
	@Excel(name = "商位转租笔数", width = 15)
    @ApiModelProperty(value = "商位转租笔数")
    @TableField("market_rent_num_1m")
    private Integer marketRentNum1m;
	/**商位转租均价（元）*/
	@Excel(name = "商位转租均价（元）", width = 15)
    @ApiModelProperty(value = "商位转租均价（元）")
    @TableField("market_rent_price_avg_1m")
    private BigDecimal marketRentPriceAvg1m;
	/**商位质押笔数*/
	@Excel(name = "商位质押笔数", width = 15)
    @ApiModelProperty(value = "商位质押笔数")
    @TableField("pledge_apply_num_1m")
    private Integer pledgeApplyNum1m;
	/**商位质押总金额（元）*/
	@Excel(name = "商位质押总金额（元）", width = 15)
    @ApiModelProperty(value = "商位质押总金额（元）")
    @TableField("pledge_apply_income_1m")
    private BigDecimal pledgeApplyIncome1m;
	/**本年招商商位户数*/
	@Excel(name = "本年招商商位户数", width = 15)
    @ApiModelProperty(value = "本年招商商位户数")
    private Integer invstHoldsBoothNumSd;
	/**本年招商商位间数*/
	@Excel(name = "本年招商商位间数", width = 15)
    @ApiModelProperty(value = "本年招商商位间数")
    private BigDecimal invstHoldsBoothRoomNumSd;
	/**当前空置户数*/
	@Excel(name = "当前空置户数", width = 15)
    @ApiModelProperty(value = "当前空置户数")
    private Integer emptyBoothHoldsNumTd;
	/**当前空置间数*/
	@Excel(name = "当前空置间数", width = 15)
    @ApiModelProperty(value = "当前空置间数")
    private BigDecimal emptyBoothRoomNumTd;
	/**本年入场资格费收入（万）*/
	@Excel(name = "本年入场资格费收入（万）", width = 15)
    @ApiModelProperty(value = "本年入场资格费收入（万）")
    private BigDecimal entryQualificationIncomeSd;
	/**剩余商位出租率统计日期 yyyy-MM-dd*/
	@Excel(name = "剩余商位出租率统计日期 yyyy-MM-dd", width = 15)
    @ApiModelProperty(value = "剩余商位出租率统计日期 yyyy-MM-dd")
    private String remainRentRateStatisticsDate;
	/**本年续租商位户数（户）*/
	@Excel(name = "本年续租商位户数（户）", width = 15)
    @ApiModelProperty(value = "本年续租商位户数（户）")
    private Integer renewLeaseHoldsBoothNumSd;
	/**本年退租商位户数（户）*/
	@Excel(name = "本年退租商位户数（户）", width = 15)
    @ApiModelProperty(value = "本年退租商位户数（户）")
    private Integer surrenderLeaseHoldsBoothNumSd;
	/**本年到期商位户数（户）*/
	@Excel(name = "本年到期商位户数（户）", width = 15)
    @ApiModelProperty(value = "本年到期商位户数（户）")
    private Integer expiredHoldsBoothNumSd;
	/**本年续租收入（万）*/
	@Excel(name = "本年续租收入（万）", width = 15)
    @ApiModelProperty(value = "本年续租收入（万）")
    private BigDecimal renewLeaseIncomeSd;
	/**续租完成率统计日期 yyyy-MM-dd*/
	@Excel(name = "续租完成率统计日期 yyyy-MM-dd", width = 15)
    @ApiModelProperty(value = "续租完成率统计日期 yyyy-MM-dd")
    private String renewLeaseRateStatisticsDate;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createdAt;
    public void customDB2VO() {
        if (this.marketTransferPriceAvg1m != null) {
            this.marketTransferPriceAvg1m = this.marketTransferPriceAvg1m.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }

        if (this.marketRentPriceAvg1m != null) {
            this.marketRentPriceAvg1m = this.marketRentPriceAvg1m.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }

        if (this.pledgeApplyIncome1m != null) {
            this.pledgeApplyIncome1m = this.pledgeApplyIncome1m.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }

        if (this.entryQualificationIncomeSd != null) {
            this.entryQualificationIncomeSd = this.entryQualificationIncomeSd.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }

        if (this.renewLeaseIncomeSd != null) {
            this.renewLeaseIncomeSd = this.renewLeaseIncomeSd.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }

        if (this.boothOpeningRate1m != null) {
            this.boothOpeningRate1m = this.boothOpeningRate1m.multiply(HUNDRED);
        }
    }

    public void customVO2DB() {
        if (this.marketTransferPriceAvg1m != null) {
            this.marketTransferPriceAvg1m = this.marketTransferPriceAvg1m.multiply(TEN_THOUSAND);
        }
        if (this.marketRentPriceAvg1m != null) {
            this.marketRentPriceAvg1m = this.marketRentPriceAvg1m.multiply(TEN_THOUSAND);
        }
        if (this.pledgeApplyIncome1m != null) {
            this.pledgeApplyIncome1m = this.pledgeApplyIncome1m.multiply(TEN_THOUSAND);
        }
        if (this.entryQualificationIncomeSd != null) {
            this.entryQualificationIncomeSd = this.entryQualificationIncomeSd.multiply(TEN_THOUSAND);
        }
        if (this.renewLeaseIncomeSd != null) {
            this.renewLeaseIncomeSd = this.renewLeaseIncomeSd.multiply(TEN_THOUSAND);
        }
        if (this.boothOpeningRate1m != null) {
            this.boothOpeningRate1m = this.boothOpeningRate1m.divide(HUNDRED, 2, RoundingMode.HALF_UP);
        }
    }

}
