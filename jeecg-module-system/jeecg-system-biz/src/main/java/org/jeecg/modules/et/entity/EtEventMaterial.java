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

    /**场景，事件场景*/
    @Excel(name = "场景", width = 15, needMerge = true)
    @ApiModelProperty(value = "场景")
    private java.lang.String scene;
    /**事件名*/
    @Excel(name = "事件名", width = 15, needMerge = true)
    @ApiModelProperty(value = "事件名")
    private java.lang.String name;
    /**中文名*/
    @Excel(name = "中文名", width = 15, needMerge = true)
    @ApiModelProperty(value = "中文名")
    private java.lang.String zhName;
    /**事件属性英文名*/
    @Excel(name = "事件属性英文名", width = 15)
    @ApiModelProperty(value = "事件属性英文名")
    private java.lang.String propertyName;
    /**事件属性中文名*/
    @Excel(name = "事件属性中文名", width = 15)
    @ApiModelProperty(value = "事件属性中文名")
    private java.lang.String propertyZhName;
    private java.lang.Integer propertyType;
    /**事件属性值类型*/
    @Excel(name = "事件属性值类型", width = 15)
    @ApiModelProperty(value = "事件属性值类型")
    private java.lang.String propertyTypeStr;
    /**事件属性值示例和说明*/
    @Excel(name = "事件属性值示例和说明", width = 15)
    @ApiModelProperty(value = "事件属性值示例和说明")
    private java.lang.String propertyExample;
    /**事件属性说明*/
    @Excel(name = "事件属性说明", width = 15)
    @ApiModelProperty(value = "事件属性说明")
    private java.lang.String propertyDesc;
    /**操作说明*/
    @Excel(name = "操作说明", width = 15, needMerge = true)
    @ApiModelProperty(value = "操作说明")
    private java.lang.String operDesc;
    private java.lang.Integer type;
    /**埋点形式*/
    @Excel(name = "埋点形式", width = 15, needMerge = true)
    @ApiModelProperty(value = "埋点形式")
    private java.lang.String typeStr;
    /**触发时机和说明*/
    @Excel(name = "触发时机和说明", width = 15, needMerge = true)
    @ApiModelProperty(value = "触发时机和说明")
    private java.lang.String triggerTiming;
    /**文档说明*/
    @Excel(name = "文档说明", width = 15, needMerge = true)
    @ApiModelProperty(value = "文档说明")
    private java.lang.String eventDesc;
    private java.lang.Integer isPresetEvent = 0;
    /**是否预置事件*/
    @Excel(name = "是否预置事件", width = 15, needMerge = true)
    @ApiModelProperty(value = "是否预置事件")
    private java.lang.String presetEventStr = "否";
    private java.lang.Integer status = 2;
    /**状态*/
    @Excel(name = "状态", width = 15, needMerge = true)
    @ApiModelProperty(value = "状态")
    private java.lang.String statusStr = "上线";

    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 15, needMerge = true, format="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**创建人*/
    @Excel(name = "创建人", width = 15, needMerge = true)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**更新时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 15, needMerge = true, format="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    /**更新人*/
    @Excel(name = "更新人", width = 15, needMerge = true)
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;

    public EtEventMaterial setType(Integer type) {
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

    public EtEventMaterial setTypeStr(String typeStr) {
        this.typeStr = typeStr;
        this.type = TYPE_MAP.get(typeStr);
        if (Objects.isNull(this.type)) {
            this.type = 0;
        }
        return this;
    }

    public EtEventMaterial setIsPresetEvent(Integer isPresetEvent) {
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

    public EtEventMaterial setPresetEventStr(String presetEventStr) {
        this.presetEventStr = presetEventStr;
        this.isPresetEvent = PRESET_EVENT_MAP.get(presetEventStr);
        if (Objects.isNull(this.isPresetEvent)) {
            this.isPresetEvent = 0;
        }
        return this;
    }

    public EtEventMaterial setStatus(Integer status) {
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

    public EtEventMaterial setStatusStr(String statusStr) {
        this.statusStr = statusStr;
        this.status = STATUS_MAP.get(statusStr);
        if (Objects.isNull(this.status)) {
            this.status = 0;
        }
        return this;
    }

    public EtEventMaterial setPropertyType(Integer propertyType) {
        this.propertyType = propertyType;
        if (StringUtils.isBlank(this.propertyTypeStr)) {
            for (Map.Entry<String, Integer> entry : PROPERTY_TYPE_MAP.entrySet()) {
                String key = entry.getKey();
                Integer val = entry.getValue();
                if (val.equals(propertyType)) {
                    this.propertyTypeStr = key;
                    break;
                }
            }
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
