package org.jeecg.modules.cg.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: CG平台店铺拉链表
 * @Author: jeecg-boot
 * @Date:   2023-04-03
 * @Version: V1.0
 */
@Data
@TableName("dim.dim_ncg_shops_zf")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CG平台店铺拉链表", description="CG平台店铺拉链表")
public class CgShopsZf implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
    @ApiModelProperty(value = "店铺编号")
    private String shopId;
	/**店铺名称*/
	@Excel(name = "店铺名称", width = 15)
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**市场名称*/
    @Excel(name = "市场名称", width = 15)
    @ApiModelProperty(value = "市场名称")
    private String marketName;
    /**商铺编号数组*/
    @Excel(name = "商铺编号数组", width = 15)
    @ApiModelProperty(value = "商铺编号数组")
    private String boothIdArr;
    /**店铺号*/
    @Excel(name = "店铺号", width = 15)
    @ApiModelProperty(value = "店铺号")
    private String boothNoArr;
}
