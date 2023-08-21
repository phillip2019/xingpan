package org.jeecg.modules.et.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.et.entity.EtEvent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.et.entity.EtEventMaterial;
import org.jeecg.modules.et.entity.EtEventMaterial2;

/**
 * @Description: et_event
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
public interface EtEventMapper extends BaseMapper<EtEvent> {

    void insertEventReturnId(EtEvent event);

    List<EtEventMaterial> list(@Param("etEvent")EtEvent etEvent, @Param("selectionList")List<String> selectionList);

    List<EtEventMaterial2> listEventMaterial2(@Param("etEvent")EtEvent etEvent, @Param("selectionList")List<String> selectionList);
}
