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

import javax.persistence.Table;

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
public class IbfMarketResource implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型")
    private String businessVersion;
    /**
     * 市场
     */
    @Dict(dicCode = "short_market_id")
    @Excel(name = "市场", width = 15, dicCode = "short_market_id")
    @ApiModelProperty(value = "市场")
    private String shortMarketId;
    /**
     * 所属年月 yyyy-MM
     */
    @Excel(name = "所属年月", width = 15, importFormat = "yyyy-MM", importConvert = true)
    @ApiModelProperty(value = "所属年月 yyyy-MM")
    private String monthCol;
    /**
     * 间数（商位）
     */
    @Excel(name = "间数(商位)", width = 15)
    @ApiModelProperty(value = "间数（商位）")
    private BigDecimal boothRoomNumTd;
    /**
     * 间数（配套）
     */
    @Excel(name = "间数(配套)", width = 15)
    @ApiModelProperty(value = "间数（配套）")
    private BigDecimal matchRoomNumTd;
    /**
     * 已出租间数（商位+配套）
     */
    @Excel(name = "已出租间数(商位+配套)", width = 15)
    @ApiModelProperty(value = "已出租间数（商位+配套）")
    @TableField("booth_match_rent_room_num_1d")
    private BigDecimal boothMatchRentRoomNum1d;
    /**
     * 面积（商位）
     */
    @Excel(name = "面积(商位)", width = 15)
    @ApiModelProperty(value = "面积（商位）")
    private BigDecimal boothAreaNumTd;
    /**
     * 面积（配套）
     */
    @Excel(name = "面积(配套)", width = 15)
    @ApiModelProperty(value = "面积（配套）")
    @TableField("match_area_num_td")
    private BigDecimal matchAreaNumTd;
    /**
     * 已出租面积(商位+配套)
     */
    @Excel(name = "已出租面积(商位+配套)", width = 15)
    @ApiModelProperty(value = "已出租面积（商位+配套）")
    @TableField("booth_match_rent_area_num_1d")
    private BigDecimal boothMatchRentAreaNum1d;
    /**
     * 资源情况统计日期 yyyy-MM-dd
     */
    @Excel(name = "资源统计日期", width = 15, importConvert = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "资源统计日期 yyyy-MM-dd")
    private Date resourceStatisticsDate;

    /**
     * 商位使用权人
     */
    @Excel(name = "商位使用权人数", width = 15)
    @ApiModelProperty(value = "商位使用权人")
    private Integer boothOwnerNum;

    /**
     * 实际经营人
     */
    @Excel(name = "实际经营人数", width = 15)
    @ApiModelProperty(value = "实际经营人数")
    private Integer boothOperatorNum;

    /**
     * 商人统计日期 yyyy-MM-dd
     */
    @Excel(name = "商人统计日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "商人统计日期 yyyy-MM-dd")
    private Date merchantStatisticsDate;

    /**
     * 本年招商间数
     */
    @Excel(name = "本年招商间数", width = 15)
    @ApiModelProperty(value = "本年招商间数")
    private BigDecimal invstRoomNumSd;

    /**
     * 当前空置间数
     */
    @Excel(name = "当前空置间数", width = 15)
    @ApiModelProperty(value = "当前空置间数")
    private BigDecimal emptyBoothRoomNumTd;

    /**
     * 剩余商位出租率统计日期 yyyy-MM-dd
     */
    @Excel(name = "出租率统计日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出租率统计日期 yyyy-MM-dd")
    private Date remainRentRateStatisticsDate;

    /**
     * 续租完成率统计日期 yyyy-MM-dd
     */
    @Excel(name = "续租统计日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "续租统计日期 yyyy-MM-dd")
    private Date renewLeaseRateStatisticsDate;

    /**
     * 人流（人次）
     */
    @Excel(name = "人流", width = 15)
    @ApiModelProperty(value = "人流（人次）")
    @TableField("market_buyer_entr_num_1m")
    private BigDecimal marketBuyerEntrNum1m;
    /**
     * 车流（人次）
     */
    @Excel(name = "车流", width = 15)
    @ApiModelProperty(value = "车流（人次）")
    @TableField("car_entr_num_1m")
    private BigDecimal carEntrNum1m;
    /**
     * 外商（人次）
     */
    @Excel(name = "外商", width = 15)
    @ApiModelProperty(value = "外商（人次）")
    @TableField("foreign_buyer_entr_num_1m")
    private BigDecimal foreignBuyerEntrNum1m;
    /**
     * 开门率 %
     */
    @Excel(name = "开门率", width = 15)
    @ApiModelProperty(value = "开门率 %")
    @TableField("booth_opening_rate_1m")
    private BigDecimal boothOpeningRate1m;
    /**
     * 市场成交额（亿）
     */
    @Excel(name = "市场成交额", width = 15)
    @ApiModelProperty(value = "市场成交额（亿）")
    @TableField("market_gmv_1m")
    private BigDecimal marketGmv1m;
    /**
     * 商位转让笔数
     */
    @Excel(name = "商位转让笔数", width = 15)
    @ApiModelProperty(value = "商位转让笔数")
    @TableField("market_transfer_num_1m")
    private Integer marketTransferNum1m;
    /**
     * 商位转让均价（元）
     */
    @Excel(name = "商位转让均价", width = 15)
    @ApiModelProperty(value = "商位转让均价（元）")
    @TableField("market_transfer_price_avg_1m")
    private BigDecimal marketTransferPriceAvg1m;
    /**
     * 商位转租笔数
     */
    @Excel(name = "商位转租笔数", width = 15)
    @ApiModelProperty(value = "商位转租笔数")
    @TableField("market_rent_num_1m")
    private Integer marketRentNum1m;
    /**
     * 商位转租均价（元）
     */
    @Excel(name = "商位转租均价", width = 15)
    @ApiModelProperty(value = "商位转租均价（元）")
    @TableField("market_rent_price_avg_1m")
    private BigDecimal marketRentPriceAvg1m;
    /**
     * 商位质押笔数
     */
    @Excel(name = "商位质押笔数", width = 15)
    @ApiModelProperty(value = "商位质押笔数")
    @TableField("pledge_apply_num_1m")
    private Integer pledgeApplyNum1m;
    /**
     * 商位质押总金额（元）
     */
    @Excel(name = "商位质押总金额", width = 15)
    @ApiModelProperty(value = "商位质押总金额（元）")
    @TableField("pledge_apply_income_1m")
    private BigDecimal pledgeApplyIncome1m;
    /**
     * 商位普通装修笔数
     */
    @Excel(name = "商位普通装修笔数", width = 15)
    @ApiModelProperty(value = "商位普通装修笔数")
    @TableField("normal_renovation_num_1m")
    private Integer normalRenovationNum1m;
    /**
     * 商位个性化装修笔数
     */
    @Excel(name = "商位个性化装修笔数", width = 15)
    @ApiModelProperty(value = "商位个性化装修笔数")
    @TableField("special_renovation_num_1m")
    private Integer specialRenovationNum1m;
    /**
     * 本年招商户数
     */
    @Excel(name = "本年招商户数", width = 15)
    @ApiModelProperty(value = "本年招商户数")
    private Integer invstHoldsNumSd;
    /**
     * 当前空置户数
     */
    @Excel(name = "当前空置户数", width = 15)
    @ApiModelProperty(value = "当前空置户数")
    private Integer emptyBoothHoldsNumTd;
    /**
     * 本年入场资格费收入（万）
     */
    @Excel(name = "本年入场资格费收入", width = 15)
    @ApiModelProperty(value = "本年入场资格费收入（万）")
    private BigDecimal entryQualificationIncomeSd;
    /**
     * 本年续租户数（户）
     */
    @Excel(name = "本年续租户数", width = 15)
    @ApiModelProperty(value = "本年续租户数（户）")
    private Integer renewLeaseHoldsNumSd;
    /**
     * 本年退租户数（户）
     */
    @Excel(name = "本年退租户数", width = 15)
    @ApiModelProperty(value = "本年退租户数（户）")
    private Integer surrenderLeaseHoldsNumSd;
    /**
     * 本年到期户数（户）
     */
    @Excel(name = "本年到期户数", width = 15)
    @ApiModelProperty(value = "本年到期户数（户）")
    private Integer expiredHoldsNumSd;
    /**
     * 本年续租收入（万）
     */
    @Excel(name = "本年续租收入", width = 15)
    @ApiModelProperty(value = "本年续租收入（万）")
    private BigDecimal renewLeaseIncomeSd;
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

    public void convertsetMonthCol(String text) {
        this.monthCol = DateUtil.convertMonthCol(text);
    }

    public String convertsetDateCol(String text) {
        return DateUtil.convertDateCol(text);
    }

    public void convertsetResourceStatisticsDate(String text) throws ParseException {
         this.resourceStatisticsDate = DateUtils.parseDate(convertsetDateCol(text), "yyyy-MM-dd");
    }

    public void convertsetMerchantStatisticsDate(String text) throws ParseException {
         this.merchantStatisticsDate = DateUtils.parseDate(convertsetDateCol(text), "yyyy-MM-dd");
    }

    public void convertsetRemainRentRateStatisticsDate(String text) throws ParseException {
        this.remainRentRateStatisticsDate = DateUtils.parseDate(convertsetDateCol(text), "yyyy-MM-dd");

    }

    public void convertsetRenewLeaseRateStatisticsDate(String text) throws ParseException {
        this.renewLeaseRateStatisticsDate = DateUtils.parseDate(convertsetDateCol(text), "yyyy-MM-dd");
    }
}
