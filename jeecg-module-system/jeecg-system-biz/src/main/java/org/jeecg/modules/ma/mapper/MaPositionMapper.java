package org.jeecg.modules.ma.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.ma.entity.MaActiveYlbQrCodeUrl;
import org.jeecg.modules.ma.entity.MaPosition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.ma.mapper.dto.MaMarketFloorNumDTO;
import org.springframework.stereotype.Repository;

/**
 * @Description: 活动点位
 * @Author: jeecg-boot
 * @Date:   2023-01-14
 * @Version: V1.0
 */
@Repository
public interface MaPositionMapper extends BaseMapper<MaPosition> {

    /**
     * 查询对应活动，市场和楼层已有点位数量
     * @param activeId 活动编号
     * @return 市场楼层对应点位数量模型
     */
    List<MaMarketFloorNumDTO> selectMarketFloorPositionNum(@Param("activeId") Long activeId);

    /**
     * 插入对应点位信息，并返回ID
     * @param maPositionDTO 活动点位
     * @return 执行成功条数
     */
    int insertPositionReturnId(@Param("dto") MaPosition maPositionDTO);

    /**
     * 通过活动编号，查询易拉宝二维码图片地址
     * @param activeId 活动编号
     * @return 返回查询内容
     */
    List<MaActiveYlbQrCodeUrl> selectYlbQrCodeByActiveId(@Param("activeId") Long activeId);
}
