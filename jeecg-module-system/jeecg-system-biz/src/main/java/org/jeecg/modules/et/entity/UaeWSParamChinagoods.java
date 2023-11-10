package org.jeecg.modules.et.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author xiaowei.song
 * @version v1.0.0
 * @description uae chinagoods websocket实体
 * @date 2023/10/30 21:24
 */
@Data
@Accessors(chain = true)
@ToString
public class UaeWSParamChinagoods implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(UaeWSParamChinagoods.class);

    private String distinctId;

    private String anonymousId;

    private String ip;

    private String event;

    private String buProjectNameId;

    private String scene;
}
