package org.jeecg.common.aspect.annotation;

import java.lang.annotation.*;

/**
 * 埋点table切换
 *
 * @author :songxiaowei@chinagoods.com
 * @date:2022-12-01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EtDynamicTable {
    /**
     * 动态解析默认表名
     * @return
     */
    String value();
}
