package org.jeecg.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;

/**
 * Jackson builder mapper
 * @author xiaowei.song
 */
public final class JacksonBuilder {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 配置jackson配置
     **/
    static {
        // 该特性决定了当遇到未知属性（没有映射到属性，没有任何setter或者任何可以处理它的handler），是否应该抛出一个JsonMappingException异常
        MAPPER.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        //在序列化时日期格式默认为 yyyy-MM-dd'T'HH:mm:ss.SSSZ
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //在序列化时忽略值为 null 的属性
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //忽略值为默认值的属性
        MAPPER.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);
        //设置JSON时间格式
        MAPPER.setDateFormat(new SimpleDateFormat(DateUtil.DEFAULT_DATE_TIME_FORMAT));
        MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        //对于日期类型为 java.time.LocalDate，还需要添加代码 mapper.registerModule(new JavaTimeModule())，同时添加相应的依赖 jar 包
        MAPPER.registerModule(new JavaTimeModule());
    }
}
