package org.jeecg.modules.et.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static org.jeecg.modules.et.entity.EtEventMaterial.PROPERTY_TYPE_MAP;

/**
 * @Description: et_event_property
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Data
@TableName("et_event_property")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="埋点事件屬性", description="埋点事件屬性")
public class EtEventProperty implements Serializable {
    private static final long serialVersionUID = 1L;

	/**事件属性表主键,自增Id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "事件属性表主键,自增Id")
    private java.lang.String id;
	/**所属事件的Id*/
    @ApiModelProperty(value = "所属事件的Id")
    private java.lang.String eventId;
	/**事件属性英文名*/
	@Excel(name = "事件属性英文名", width = 15)
    @ApiModelProperty(value = "事件属性英文名")
    private java.lang.String name;
	/**事件属性中文名*/
	@Excel(name = "事件属性中文名", width = 15)
    @ApiModelProperty(value = "事件属性中文名")
    private java.lang.String zhName;
	/**事件属性值类型值*/
    @ApiModelProperty(value = "事件属性值类型值")
    private java.lang.Integer type;

    @Excel(name = "事件属性值类型", width = 15)
    @ApiModelProperty(value = "事件属性值类型")
    @TableField(exist = false)
    private  java.lang.String typeStr;

	/**事件属性值示例*/
	@Excel(name = "事件属性值示例", width = 15)
    @ApiModelProperty(value = "事件属性值示例")
    private java.lang.String example;
	/**事件属性说明*/
	@Excel(name = "事件属性说明", width = 15)
    @ApiModelProperty(value = "事件属性说明")
    private java.lang.String propertyDesc;
    /**序号*/
    @Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private java.lang.Integer sorted;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;

    public EtEventProperty setType(Integer type) {
        this.type = type;
        if (StringUtils.isBlank(this.typeStr)) {
            for (Map.Entry<String, Integer> entry : PROPERTY_TYPE_MAP.entrySet()) {
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

    public EtEventProperty setTypeStr(String typeStr) {
        this.typeStr = typeStr;
        this.type = PROPERTY_TYPE_MAP.get(typeStr);
        if (Objects.isNull(this.type)) {
            this.type = 0;
        }
        return this;
    }
}
