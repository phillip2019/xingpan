package org.jeecg.modules.ma.mapper;

import org.jeecg.modules.ma.entity.MaActiveTaiKaQrCodeUrl;
import org.jeecg.modules.ma.entity.MaTaiKaShop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: ma_tai_ka_shop
 * @Author: jeecg-boot
 * @Date:   2023-04-03
 * @Version: V1.0
 */
@Repository
public interface MaTaiKaShopMapper extends BaseMapper<MaTaiKaShop> {

    /**
     * 通过活动编号查询台卡店铺微信二维码
     * @param activeId 活动编号
     * @return 台卡店铺微信二维码列表
     **/
    List<MaActiveTaiKaQrCodeUrl> selectTaiKaQrCodeByActiveId(Long activeId);
}
