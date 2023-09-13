package org.jeecg.modules.et.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.et.entity.EtClientEvent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: et_client_event
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
public interface EtClientEventMapper extends BaseMapper<EtClientEvent> {

    List<String> listEventIdByClientName(@Param("clientName") String clientName);
}
