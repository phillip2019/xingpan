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

    /**业务类型*/
    @ApiModelProperty(value = "业务类型")
    private String businessVersion;
}
