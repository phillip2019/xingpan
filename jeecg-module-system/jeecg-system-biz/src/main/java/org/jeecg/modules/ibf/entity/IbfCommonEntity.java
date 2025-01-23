package org.jeecg.modules.ibf.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecg.common.util.DateUtil;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class IbfCommonEntity extends JeecgEntity implements Serializable {

    /**市场*/
    @Dict(dicCode = "finance_short_market_id")
    @Excel(name = "市场", width = 4, dicCode = "finance_short_market_id", type = 1)
    @ApiModelProperty(value = "市场ID")
    private String shortMarketId;

    /**所属年月 yyyy-MM*/
    @Excel(name = "月份", width = 8, importConvert = true, type = 1)
    @ApiModelProperty(value = "所属年月")
    private String monthCol;

    /**是否发布*/
    @Dict(dicCode = "is_publish")
    @Excel(name = "状态", width = 8, importConvert = true, type = 1, dicCode = "is_publish")
    @ApiModelProperty(value = "是否发布")
    private Integer isPublish;

    /**
     * 当前月份标记
     * 0: 当前月份
     * 1: 前1个月
     * */
    @ApiModelProperty(value = "当前月份标记")
    private Integer flag;

    /**
     * 当前版本,是否可见
     * 0: 不可见
     * 1: 可见
     * */
    @Dict(dicCode = "is_visible")
    @ApiModelProperty(value = "是否可见")
    private Integer isVisible;

    /**是否删除*/
    @Dict(dicCode = "is_deleted")
    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    public void convertsetMonthCol(String text) {
        this.monthCol = DateUtil.convertMonthCol(text);
    }

    /**
     * 特殊处理方法，某些参数需要转移，除特定的值
     * 将数据库中的值转换成VO
     **/
    public void customDB2VO() {
    }

    /**
     * 特殊处理方法，某些参数需要转移，乘特定的值
     * 将VO转换成DB值
     **/
    public void customVO2DB() {
    }

    public void convertsetIsPublish(Integer text) {
        this.isPublish = text;
    }
}
