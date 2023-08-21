package org.jeecg.modules.et.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="事件导出模型", description="事件导出模型")
public class EtEventScene implements Serializable {
    private static final long serialVersionUID = 1L;

    /**场景，事件场景*/
    @Excel(name = "场景", width = 15, needMerge = true)
    @ApiModelProperty(value = "场景")
    private String scene;

    @ExcelCollection(name = "事件")
    @ApiModelProperty(value = "事件")
    private List<EtEventMaterial2> eventMaterial2List;
}
