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
 * @Description: et_client
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
@Data
@TableName("et_client")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="et_client对象", description="et_client")
public class EtClient implements Serializable {
    private static final long serialVersionUID = 1L;

	/**客户端编号*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "客户端编号")
    private java.lang.Integer id;
	/**客户端名称*/
	@Excel(name = "客户端名称", width = 15)
    @ApiModelProperty(value = "客户端名称")
    private java.lang.String name;
	/**客户端网址*/
	@Excel(name = "客户端网址", width = 15)
    @ApiModelProperty(value = "客户端网址")
    private java.lang.String url;
	/**是否移动端*/
	@Excel(name = "是否移动端", width = 15)
    @ApiModelProperty(value = "是否移动端")
    private java.lang.Integer isMobile;
	/**平台站点*/
	@Excel(name = "平台站点", width = 15)
    @ApiModelProperty(value = "平台站点")
    private java.lang.String platformSite;
	/**平台站点编码*/
	@Excel(name = "平台站点编码", width = 15)
    @ApiModelProperty(value = "平台站点编码")
    private java.lang.Integer platformSiteCodeId;
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
