package org.jeecg.modules.et.entity;

import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="事件导入模型", description="事件导入模型")
public class EtEventMaterial implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final Map<String, Integer> TYPE_MAP = ImmutableMap.<String, Integer>builder()
            .put("前端", 1)
            .put("后端", 2)
            .build();

    public static final Map<String, Integer> PRESET_EVENT_MAP = ImmutableMap.<String, Integer>builder()
            .put("是", 1)
            .put("否", 2)
            .build();

    public static final Map<String, Integer> STATUS_MAP = ImmutableMap.<String, Integer>builder()
            .put("初始化", 1)
            .put("上线", 2)
            .put("下线", 3)
            .put("异常", 4)
            .build();

    public static final Map<String, Integer> PROPERTY_TYPE_MAP = ImmutableMap.<String, Integer>builder()
            .put("字符串", 1)
            .put("数值", 2)
            .put("BOOL", 3)
            .put("列表", 4)
            .build();

    /**事件名*/
    @Excel(name = "事件名", width = 15)
    @ApiModelProperty(value = "事件名")
    private java.lang.String name;
    /**中文名*/
    @Excel(name = "中文名", width = 15)
    @ApiModelProperty(value = "中文名")
    private java.lang.String zhName;
    /**操作说明*/
    @Excel(name = "操作说明", width = 15)
    @ApiModelProperty(value = "操作说明")
    private java.lang.String operDesc;
    private java.lang.Integer type;
    /**埋点形式*/
    @Excel(name = "埋点形式", width = 15)
    @ApiModelProperty(value = "埋点形式")
    private java.lang.String typeStr;
    /**触发时机*/
    @Excel(name = "触发时机", width = 15)
    @ApiModelProperty(value = "触发时机")
    private java.lang.String triggerTiming;
    /**文档说明*/
    @Excel(name = "文档说明", width = 15)
    @ApiModelProperty(value = "文档说明")
    private java.lang.String eventDesc;
    private java.lang.Integer isPresetEvent = 0;
    /**是否预置事件*/
    @Excel(name = "是否预置事件", width = 15)
    @ApiModelProperty(value = "是否预置事件")
    private java.lang.String presetEventStr = "否";
    /**场景，事件场景*/
    @Excel(name = "场景", width = 15)
    @ApiModelProperty(value = "场景")
    private java.lang.String scene;
    private java.lang.Integer status = 2;
    /**状态*/
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.String statusStr = "上线";

    /**事件属性英文名*/
    @Excel(name = "事件属性英文名", width = 15)
    @ApiModelProperty(value = "事件属性英文名")
    private java.lang.String propertyName;
    /**事件属性中文名*/
    @Excel(name = "事件属性中文名", width = 15)
    @ApiModelProperty(value = "事件属性英文名")
    private java.lang.String propertyZhName;
    private java.lang.Integer propertyType;
    /**事件属性值类型*/
    @Excel(name = "事件属性值类型", width = 15)
    @ApiModelProperty(value = "事件属性值类型")
    private java.lang.String propertyTypeStr;
    /**事件属性值示例*/
    @Excel(name = "事件属性值示例", width = 15)
    @ApiModelProperty(value = "事件属性值示例")
    private java.lang.String propertyExample;
    /**事件属性说明*/
    @Excel(name = "事件属性说明", width = 15)
    @ApiModelProperty(value = "事件属性说明")
    private java.lang.String propertyDesc;

    public EtEventMaterial setTypeStr(String typeStr) {
        this.typeStr = typeStr;
        this.type = TYPE_MAP.get(typeStr);
        if (Objects.isNull(this.type)) {
            this.type = 0;
        }
        return this;
    }

    public EtEventMaterial setPresetEventStr(String presetEventStr) {
        this.presetEventStr = presetEventStr;
        this.isPresetEvent = PRESET_EVENT_MAP.get(presetEventStr);
        if (Objects.isNull(this.isPresetEvent)) {
            this.isPresetEvent = 0;
        }
        return this;
    }

    public EtEventMaterial setStatusStr(String statusStr) {
        this.statusStr = statusStr;
        this.status = STATUS_MAP.get(statusStr);
        if (Objects.isNull(this.status)) {
            this.status = 0;
        }
        return this;
    }

    public EtEventMaterial setPropertyTypeStr(String propertyTypeStr) {
        this.propertyTypeStr = propertyTypeStr;
        this.propertyType = PROPERTY_TYPE_MAP.get(propertyTypeStr);
        if (Objects.isNull(this.propertyType)) {
            this.propertyType = 0;
        }
        return this;
    }
}
