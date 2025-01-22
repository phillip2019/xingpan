package org.jeecg.modules.ibf.entity;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static org.jeecg.modules.ibf.IbfConst.TEN_THOUSAND;

/**
 * @Description: 业财一体-市场资源填报表
 * @Author: jeecg-boot
 * @Date: 2024-12-19
 * @Version: V1.0
 */
@Data
@TableName("ibf_market_resource")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ibf_market_resource对象", description = "业财一体-市场资源填报表")
public class IbfMarketResource extends IbfCommonEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 资源情况统计日期 yyyy-MM-dd
     */
    @Excel(name = "资源统计日期", width = 15, importConvert = true, type = 4)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private String resourceStatisticsDate;

    /**
     * 间数（商位）
     */
    @Excel(name = "间数(商位)", width = 15, groupName="间数", type = 4)
    @ApiModelProperty(value = "间数（商位）")
    private BigDecimal boothRoomNumTd;
    /**
     * 间数（配套）
     */
    @Excel(name = "间数(配套)", width = 15, groupName="间数", type = 4)
    @ApiModelProperty(value = "间数（配套）")
    private BigDecimal matchRoomNumTd;
    /**
     * 已出租间数（商位+配套）
     */
    @Excel(name = "已出租间数(商位+配套)", width = 30, groupName="间数", type = 4)
    @ApiModelProperty(value = "已出租间数（商位+配套）")
    @TableField("booth_match_rent_room_num_1d")
    private BigDecimal boothMatchRentRoomNumTd;
    /**
     * 面积（商位）
     */
    @Excel(name = "面积(商位)", width = 15, groupName="面积", type = 4)
    @ApiModelProperty(value = "面积（商位）")
    private BigDecimal boothAreaNumTd;
    /**
     * 面积（配套）
     */
    @Excel(name = "面积(配套)", width = 15, groupName="面积", type = 4)
    @ApiModelProperty(value = "面积（配套）")
    @TableField("match_area_num_td")
    private BigDecimal matchAreaNumTd;
    /**
     * 已出租面积(商位+配套)
     */
    @Excel(name = "已出租面积(商位+配套)", width = 30, groupName="面积", type = 4)
    @ApiModelProperty(value = "已出租面积（商位+配套）")
    @TableField("booth_match_rent_area_num_1d")
    private BigDecimal boothMatchRentAreaNumTd;

    /**
     * 商人统计日期 yyyy-MM-dd
     */
    @Excel(name = "商人统计日期", width = 15, importConvert = true, type = 1, groupName = "商人")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private String merchantStatisticsDate;

    /**
     * 商位使用权人
     */
    @Excel(name = "商位使用权人数", width = 15, type = 4, groupName = "商人")
    @ApiModelProperty(value = "商位使用权人")
    private Integer boothOwnerNum;

    /**
     * 实际经营人
     */
    @Excel(name = "实际经营人数", width = 15, type = 4, groupName = "商人")
    @ApiModelProperty(value = "实际经营人数")
    private Integer boothOperatorNum;


    /**
     * 剩余商位出租率统计日期 yyyy-MM-dd
     */
    @Excel(name = "剩余商位出租率统计日期", width = 30, importConvert = true, type = 1, groupName = "招商")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private String remainRentRateStatisticsDate;

    /**
     * 本年招商间数
     */
    @Excel(name = "本年招商间数", width = 15, type = 4, groupName = "招商")
    @ApiModelProperty(value = "本年招商间数")
    private BigDecimal invstRoomNumSd;

    /**
     * 当前空置间数
     */
    @Excel(name = "当前空置间数", width = 15, type = 4, groupName = "招商")
    @ApiModelProperty(value = "当前空置间数")
    private BigDecimal emptyBoothRoomNumTd;

    /**
     * 本年招商户数
     */
    @Excel(name = "本年招商户数", width = 15, type = 4, groupName = "招商")
    @ApiModelProperty(value = "本年招商户数")
    private Integer invstHoldsNumSd;
    /**
     * 当前空置户数
     */
    @Excel(name = "当前空置户数", width = 15, type = 4, groupName = "招商")
    @ApiModelProperty(value = "当前空置户数")
    private Integer emptyBoothHoldsNumTd;
    /**
     * 本年入场资格费收入（万元）
     */
    @Excel(name = "本年入场资格费收入", width = 20, type = 4, groupName = "招商")
    @ApiModelProperty(value = "本年入场资格费收入（万元）")
    private BigDecimal entryQualificationIncomeSd;
    /**
     * 续租完成率统计日期 yyyy-MM-dd
     */
    @Excel(name = "续租统计日期", width = 15, importConvert = true, type = 1, groupName = "续租")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private String renewLeaseRateStatisticsDate;
    /**
     * 本年续租户数（户）
     */
    @Excel(name = "本年续租户数", width = 15, type = 4, groupName = "续租")
    @ApiModelProperty(value = "本年续租户数（户）")
    private Integer renewLeaseHoldsNumSd;

    /**
     * 本年退租户数（户）
     */
    @Excel(name = "本年退租户数", width = 15, type = 4, groupName = "续租")
    @ApiModelProperty(value = "本年退租户数（户）")
    private Integer surrenderLeaseHoldsNumSd;
    /**
     * 本年到期户数（户）
     */
    @Excel(name = "本年到期户数", width = 15, type = 4, groupName = "续租")
    @ApiModelProperty(value = "本年到期户数（户）")
    private Integer expiredHoldsNumSd;
    /**
     * 本年续租收入（万元）
     */
    @Excel(name = "本年续租收入", width = 15, type = 4, groupName = "续租")
    @ApiModelProperty(value = "本年续租收入（万元）")
    private BigDecimal renewLeaseIncomeSd;
    /**
     * 商位转让笔数
     */
    @Excel(name = "商位转让笔数", width = 15, type = 4, groupName = "转让")
    @ApiModelProperty(value = "商位转让笔数")
    @TableField("market_transfer_num_1m")
    private Integer marketTransferNum1m;
    /**
     * 商位转让均价（元）
     */
    @Excel(name = "商位转让均价", width = 15, type = 4, groupName = "转让")
    @ApiModelProperty(value = "商位转让均价（元）")
    @TableField("market_transfer_price_avg_1m")
    private BigDecimal marketTransferPriceAvg1m;
    /**
     * 商位转租笔数
     */
    @Excel(name = "商位转租笔数", width = 15, type = 4, groupName = "转租")
    @ApiModelProperty(value = "商位转租笔数")
    @TableField("market_rent_num_1m")
    private Integer marketRentNum1m;
    /**
     * 商位转租均价（元）
     */
    @Excel(name = "商位转租均价", width = 15, type = 4, groupName = "转租")
    @ApiModelProperty(value = "商位转租均价（元）")
    @TableField("market_rent_price_avg_1m")
    private BigDecimal marketRentPriceAvg1m;
    /**
     * 商位质押笔数
     */
    @Excel(name = "商位质押笔数", width = 15, type = 4, groupName = "质押")
    @ApiModelProperty(value = "商位质押笔数")
    @TableField("pledge_apply_num_1m")
    private Integer pledgeApplyNum1m;
    /**
     * 商位质押总金额（元）
     */
    @Excel(name = "商位质押总金额", width = 15, type = 4, groupName = "质押")
    @ApiModelProperty(value = "商位质押总金额（元）")
    @TableField("pledge_apply_income_1m")
    private BigDecimal pledgeApplyIncome1m;
    /**
     * 商位普通装修笔数
     */
    @Excel(name = "普通装修笔数", width = 15, type = 4, groupName = "装修")
    @ApiModelProperty(value = "商位普通装修笔数")
    @TableField("normal_renovation_num_1m")
    private Integer normalRenovationNum1m;
    /**
     * 商位个性化装修笔数
     */
    @Excel(name = "个性化装修笔数", width = 15, type = 4, groupName = "装修")
    @ApiModelProperty(value = "商位个性化装修笔数")
    @TableField("special_renovation_num_1m")
    private Integer specialRenovationNum1m;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateBy;

    public String convertsetDateCol(String text) {
        return DateUtil.convertDateCol(text);
    }

    public void convertsetResourceStatisticsDate(String text) throws ParseException {
         this.resourceStatisticsDate = convertsetDateCol(text);
    }

    public void convertsetMerchantStatisticsDate(String text) throws ParseException {
         this.merchantStatisticsDate = convertsetDateCol(text);
    }

    public void convertsetRemainRentRateStatisticsDate(String text) throws ParseException {
        this.remainRentRateStatisticsDate = convertsetDateCol(text);

    }

    public void convertsetRenewLeaseRateStatisticsDate(String text) throws ParseException {
        this.renewLeaseRateStatisticsDate = convertsetDateCol(text);
    }

    @Override
    public void customDB2VO() {
        super.customDB2VO();
        // 金额都/10000
        if (this.entryQualificationIncomeSd != null) {
            this.entryQualificationIncomeSd = this.entryQualificationIncomeSd.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }

        if (this.renewLeaseIncomeSd != null) {
            this.renewLeaseIncomeSd = this.renewLeaseIncomeSd.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }

        if (this.marketTransferPriceAvg1m != null) {
            this.marketTransferPriceAvg1m = this.marketTransferPriceAvg1m.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }

        if (this.marketRentPriceAvg1m != null) {
            this.marketRentPriceAvg1m = this.marketRentPriceAvg1m.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }

        if (this.pledgeApplyIncome1m != null) {
            this.pledgeApplyIncome1m = this.pledgeApplyIncome1m.divide(TEN_THOUSAND, 2, RoundingMode.HALF_UP);
        }

    }

    @Override
    public void customVO2DB() {
        super.customVO2DB();
        if (this.entryQualificationIncomeSd != null) {
            this.entryQualificationIncomeSd = this.entryQualificationIncomeSd.multiply(TEN_THOUSAND);
        }
        if (this.renewLeaseIncomeSd != null) {
            this.renewLeaseIncomeSd = this.renewLeaseIncomeSd.multiply(TEN_THOUSAND);
        }
        if (this.marketTransferPriceAvg1m != null) {
            this.marketTransferPriceAvg1m = this.marketTransferPriceAvg1m.multiply(TEN_THOUSAND);
        }
        if (this.marketRentPriceAvg1m != null) {
            this.marketRentPriceAvg1m = this.marketRentPriceAvg1m.multiply(TEN_THOUSAND);
        }
        if (this.pledgeApplyIncome1m != null) {
            this.pledgeApplyIncome1m = this.pledgeApplyIncome1m.multiply(TEN_THOUSAND);
        }
    }
}
