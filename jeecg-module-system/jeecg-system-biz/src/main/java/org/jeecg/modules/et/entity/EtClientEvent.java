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
 * @Description: et_client_event
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Data
@TableName("et_client_event")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="et_client_event对象", description="et_client_event")
public class EtClientEvent implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID，自增*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID，自增")
    private java.lang.String id;
	/**客户端编号*/
	@Excel(name = "客户端编号", width = 15)
    @ApiModelProperty(value = "客户端编号")
    private java.lang.String clientId;
	/**事件编号*/
	@Excel(name = "事件编号", width = 15)
    @ApiModelProperty(value = "事件编号")
    private java.lang.String eventId;
	/**状态 1-初始化 2-上线 3-下线 4-异常*/
	@Excel(name = "状态 1-初始化 2-上线 3-下线 4-异常", width = 15)
    @ApiModelProperty(value = "状态 1-初始化 2-上线 3-下线 4-异常")
    private java.lang.Integer status;
	/**截图地址*/
	@Excel(name = "截图地址", width = 15)
    @ApiModelProperty(value = "截图地址")
    private java.lang.String screenshot;
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
