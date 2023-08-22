package org.jeecg.modules.et.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: et_event
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Data
@TableName("et_event")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="埋点事件对象", description="埋点事件")
public class EtEvent implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID，自增*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private java.lang.String id;
    /**场景，事件场景*/
    @Excel(name = "场景", width = 15)
    @ApiModelProperty(value = "场景")
    private java.lang.String scene;
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
	/**埋点形式 1-前端 2-后端*/
	@Excel(name = "埋点形式", width = 15)
    @ApiModelProperty(value = "埋点形式")
    private java.lang.Integer type;
	/**触发时机*/
	@Excel(name = "触发时机", width = 15)
    @ApiModelProperty(value = "触发时机")
    private java.lang.String triggerTiming;
	/**文档说明*/
	@Excel(name = "文档说明", width = 15)
    @ApiModelProperty(value = "文档说明")
    private java.lang.String eventDesc;
	/**是否预置事件*/
	@Excel(name = "是否预置事件", width = 15)
    @ApiModelProperty(value = "是否预置事件")
    private java.lang.Integer isPresetEvent;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;

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
}
