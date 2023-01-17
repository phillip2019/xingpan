package org.jeecg.modules.demo.ma.mapper.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: 市场楼层统计
 * @Author xiaowei.song
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MaMarketFloorNumDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String marketName;
    private String floor;
    private Integer num;
}
