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
 * @Description: 平台标准化页面名称规则
 * @Author: jeecg-boot
 * @Date:   2024-01-31
 * @Version: V1.0
 */
@Data
@TableName("web_path_map_pip")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="web_path_map_pip对象", description="平台标准化页面名称规则")
public class WebPathMapPip implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**项目名*/
	@Excel(name = "项目名", width = 15)
    @ApiModelProperty(value = "项目名")
    private String project;
	/**客户端类型*/
	@Excel(name = "客户端类型", width = 15)
    @ApiModelProperty(value = "客户端类型")
    private String platformType;
	/**语言*/
	@Excel(name = "语言", width = 15)
    @ApiModelProperty(value = "语言")
    private String platformLang;
	/**模块*/
	@Excel(name = "模块", width = 15)
    @ApiModelProperty(value = "模块")
    private String unit;
	/**子模块*/
	@Excel(name = "子模块", width = 15)
    @ApiModelProperty(value = "子模块")
    private String subUnit;
	/**访问页面备注名称*/
	@Excel(name = "访问页面备注名称", width = 15)
    @ApiModelProperty(value = "访问页面备注名称")
    private String pageName;
	/**页面路径*/
	@Excel(name = "页面路径", width = 15)
    @ApiModelProperty(value = "页面路径")
    private String path;
	/**参数*/
	@Excel(name = "参数", width = 15)
    @ApiModelProperty(value = "参数")
    private String parameter;
}
