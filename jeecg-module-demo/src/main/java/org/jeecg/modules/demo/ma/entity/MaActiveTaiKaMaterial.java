package org.jeecg.modules.demo.ma.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @author xiaowei.song
 * @version v1.0.0
 * @description TODO
 * @date 2023/4/3 22:42
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ma_active_tai_ka_material对象", description="活动商铺台卡物料")
public class MaActiveTaiKaMaterial  implements Serializable {
    private static final long serialVersionUID = 1L;
    /**店铺编号*/
    @Excel(name = "店铺编号", width = 15)
    @ApiModelProperty(value = "店铺编号")
    private String shopId;
}
