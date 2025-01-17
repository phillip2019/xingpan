package org.jeecg.modules.ibf.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

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
    @Excel(name = "月份", width = 8, importConvert = true, type = 4)
    @ApiModelProperty(value = "所属年月")
    private String monthCol;

    /**是否发布*/
    @Dict(dicCode = "is_publish")
    @ApiModelProperty(value = "是否发布")
    private Integer isPublish;

    /**
     * 当前月份标记
     * 0: 当前月份
     * 1: 前1个月
     * */
    @ApiModelProperty(value = "当前月份标记")
    private transient Integer flag = 0;

}
