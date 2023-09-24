package org.jeecg.modules.et.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.et.entity.EtEventLocal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: CK中实时埋点事件
 * @Author: jeecg-boot
 * @Date:   2023-08-24
 * @Version: V1.0
 */
@Repository
public interface EtEventLocalMapper extends BaseMapper<EtEventLocal> {

}
