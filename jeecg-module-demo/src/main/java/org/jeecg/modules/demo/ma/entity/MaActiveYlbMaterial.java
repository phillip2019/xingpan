package org.jeecg.modules.demo.ma.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 易拉宝物料
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ma_active_ylb_material对象", description="活动易拉宝物料")
public class MaActiveYlbMaterial implements Serializable {
    private static final long serialVersionUID = 1L;

	/**市场名称*/
	@Excel(name = "市场", width = 15)
    @ApiModelProperty(value = "市场")
    private String marketName;
    /**序号*/
    @Excel(name = "易拉宝序号", width = 15)
    @ApiModelProperty(value = "易拉宝序号")
    private String seqNo;
	/**楼层*/
	@Excel(name = "楼层", width = 15)
    @ApiModelProperty(value = "楼层")
    private String floor;
    /**位置类型*/
    @Excel(name = "位置类型", width = 15)
    @ApiModelProperty(value = "位置类型")
    private String positionAddressType;
    /**具体位置*/
    @Excel(name = "具体位置", width = 15)
    @ApiModelProperty(value = "具体位置")
    private String positionAddressDetail;
    /**市场行业*/
    @Excel(name = "市场行业", width = 15)
    @ApiModelProperty(value = "市场行业")
    private String industryName;
    /**管理员账号*/
    @Excel(name = "管理员账号", width = 15)
    @ApiModelProperty(value = "管理员账号")
    private String ownerAccount;
    /**管理员名字*/
    @Excel(name = "管理员名字", width = 15)
    @ApiModelProperty(value = "管理员名字")
    private String ownerName;
    /**店铺ID*/
    @Excel(name = "店铺ID", width = 15)
    @ApiModelProperty(value = "店铺ID")
    private String shopId;
    /**店铺名称*/
    @Excel(name = "店铺名称", width = 15)
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**店铺位置*/
    @Excel(name = "店铺位置", width = 15)
    @ApiModelProperty(value = "店铺位置")
    private String shopAddress;
    /**经营年限*/
    @Excel(name = "经营年限", width = 15)
    @ApiModelProperty(value = "经营年限")
    private String entryYearCnt;
    /**店铺介绍*/
    @Excel(name = "店铺介绍", width = 15)
    @ApiModelProperty(value = "店铺介绍")
    private String shopIntroduction;
    /**主营内容*/
    @Excel(name = "主营内容", width = 15)
    @ApiModelProperty(value = "主营内容")
    private String mainBusinessScope;
    /**店铺主图链接*/
    @Excel(name = "店铺主图链接", width = 100)
    @ApiModelProperty(value = "店铺主图链接")
    private String shopCover;
    /**店铺主营商品数组*/
    @Excel(name = "店铺主营商品数组", width = 100)
    @ApiModelProperty(value = "店铺主营商品数组")
    private String mainGoodsArr;
    /**店铺位置*/
    @Excel(name = "店铺位置", width = 15)
    @ApiModelProperty(value = "店铺位置")
    private String shopLocation;
}
