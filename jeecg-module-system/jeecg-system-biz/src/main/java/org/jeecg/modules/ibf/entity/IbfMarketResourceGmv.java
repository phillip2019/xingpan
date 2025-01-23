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

import static org.jeecg.modules.ibf.IbfConst.HUNDRED_MILLION;
import static org.jeecg.modules.ibf.IbfConst.TEN_THOUSAND;

/**
 * @Description: 月市场资源-市场成交额填报
 * @Author: jeecg-boot
 * @Date:   2025-01-15
 * @Version: V1.0
 */
@Data
@TableName("ibf_market_resource_gmv")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ibf_market_resource_gmv对象", description="月市场资源-市场成交额填报")
public class IbfMarketResourceGmv extends IbfCommonEntity implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**市场名称*/
    @ApiModelProperty(value = "市场名称")
    private String shortMarketName;
	/**市场成交额（亿）*/
	@Excel(name = "市场成交额（亿）", width = 15)
    @ApiModelProperty(value = "市场成交额（亿）")
    @TableField("market_gmv_1m")
    private BigDecimal marketGmv1m;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
        if (this.marketGmv1m != null) {
            this.marketGmv1m = this.marketGmv1m.divide(HUNDRED_MILLION, 2, RoundingMode.HALF_UP);
        }
    }

    @Override
    public void customVO2DB() {
        super.customVO2DB();
        if (this.marketGmv1m != null) {
            this.marketGmv1m = this.marketGmv1m.multiply(HUNDRED_MILLION);
        }
    }
}
