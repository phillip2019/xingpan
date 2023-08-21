package org.jeecg.modules.et.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.jeecg.modules.et.entity.EtEventMaterial.*;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="事件导出模型", description="事件导出模型")
public class EtEventMaterial2 implements Serializable {
    private static final long serialVersionUID = 1L;

    /**ID*/
    @ApiModelProperty(value = "ID")
    private String id;
    /**场景*/
    @Excel(name = "场景", width = 15, needMerge = true)
    @ApiModelProperty(value = "场景")
    private String scene;
    /**事件名*/
    @Excel(name = "事件名", width = 15, needMerge = true)
    @ApiModelProperty(value = "事件名")
    private String name;
    /**中文名*/
    @Excel(name = "中文名", width = 15, needMerge = true)
    @ApiModelProperty(value = "中文名")
    private String zhName;
    @ExcelCollection(name = "事件属性")
    @ApiModelProperty(value = "事件属性")
    private List<EtEventProperty> propertyList;
    /**操作说明*/
    @Excel(name = "操作说明", width = 15, needMerge = true)
    @ApiModelProperty(value = "操作说明")
    private String operDesc;
    private Integer type;
    /**埋点形式*/
    @Excel(name = "埋点形式", width = 15, needMerge = true)
    @ApiModelProperty(value = "埋点形式")
    private String typeStr;
    /**触发时机和说明*/
    @Excel(name = "触发时机和说明", width = 15, needMerge = true)
    @ApiModelProperty(value = "触发时机和说明")
    private String triggerTiming;
    /**文档说明*/
    @Excel(name = "文档说明", width = 15, needMerge = true)
    @ApiModelProperty(value = "文档说明")
    private String eventDesc;
    private Integer isPresetEvent = 0;
    /**是否预置事件*/
    @Excel(name = "是否预置事件", width = 15, needMerge = true)
    @ApiModelProperty(value = "是否预置事件")
    private String presetEventStr = "否";
    private Integer status = 2;
    /**状态*/
    @Excel(name = "状态", width = 15, needMerge = true)
    @ApiModelProperty(value = "状态")
    private String statusStr = "上线";

    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 15, needMerge = true, format="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**创建人*/
    @Excel(name = "创建人", width = 15, needMerge = true)
    @ApiModelProperty(value = "创建人")
    private String createBy;
    public EtEventMaterial2 setType(Integer type) {
        this.type = type;
        if (StringUtils.isBlank(this.typeStr)) {
            for (Map.Entry<String, Integer> entry : TYPE_MAP.entrySet()) {
                String key = entry.getKey();
                Integer val = entry.getValue();
                if (val.equals(type)) {
                    this.typeStr = key;
                    break;
                }
            }
        }
        return this;
    }

    public EtEventMaterial2 setTypeStr(String typeStr) {
        this.typeStr = typeStr;
        this.type = TYPE_MAP.get(typeStr);
        if (Objects.isNull(this.type)) {
            this.type = 0;
        }
        return this;
    }

    public EtEventMaterial2 setIsPresetEvent(Integer isPresetEvent) {
        this.isPresetEvent = isPresetEvent;
        if (StringUtils.isBlank(this.presetEventStr)) {
            for (Map.Entry<String, Integer> entry : PRESET_EVENT_MAP.entrySet()) {
                String key = entry.getKey();
                Integer val = entry.getValue();
                if (val.equals(isPresetEvent)) {
                    this.presetEventStr = key;
                    break;
                }
            }
        }
        return this;
    }

    public EtEventMaterial2 setPresetEventStr(String presetEventStr) {
        this.presetEventStr = presetEventStr;
        this.isPresetEvent = PRESET_EVENT_MAP.get(presetEventStr);
        if (Objects.isNull(this.isPresetEvent)) {
            this.isPresetEvent = 0;
        }
        return this;
    }

    public EtEventMaterial2 setStatus(Integer status) {
        this.status = status;
        if (StringUtils.isBlank(this.statusStr)) {
            for (Map.Entry<String, Integer> entry : STATUS_MAP.entrySet()) {
                String key = entry.getKey();
                Integer val = entry.getValue();
                if (val.equals(status)) {
                    this.statusStr = key;
                    break;
                }
            }
        }
        return this;
    }

    public EtEventMaterial2 setStatusStr(String statusStr) {
        this.statusStr = statusStr;
        this.status = STATUS_MAP.get(statusStr);
        if (Objects.isNull(this.status)) {
            this.status = 0;
        }
        return this;
    }
}
