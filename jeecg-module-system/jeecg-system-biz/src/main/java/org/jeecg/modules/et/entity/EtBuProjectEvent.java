package org.jeecg.modules.demo.org.jeecg.et.entity;

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
 * @Description: et_bu_project_event
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Data
@TableName("et_bu_project_event")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="et_bu_project_event对象", description="et_bu_project_event")
public class EtBuProjectEvent implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID，自增*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID，自增")
    private java.lang.Integer id;
	/**埋点项目编号*/
	@Excel(name = "埋点项目编号", width = 15)
    @ApiModelProperty(value = "埋点项目编号")
    private java.lang.Integer buProjectId;
	/**事件编号*/
	@Excel(name = "事件编号", width = 15)
    @ApiModelProperty(value = "事件编号")
    private java.lang.Integer eventId;
	/**负责人*/
	@Excel(name = "负责人", width = 15)
    @ApiModelProperty(value = "负责人")
    private java.lang.String owner;
	/**测试负责人*/
	@Excel(name = "测试负责人", width = 15)
    @ApiModelProperty(value = "测试负责人")
    private java.lang.String teOwner;
	/**状态 1-初始化 2-埋点中 3-测试中 4-测试完成 5-上线*/
	@Excel(name = "状态 1-初始化 2-埋点中 3-测试中 4-测试完成 5-上线", width = 15)
    @ApiModelProperty(value = "状态 1-初始化 2-埋点中 3-测试中 4-测试完成 5-上线")
    private java.lang.Integer status;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
}
