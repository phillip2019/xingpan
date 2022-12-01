package org.jeecg.config.mybatis.aspect;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.common.aspect.annotation.EtDynamicTable;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.config.mybatis.ThreadLocalDataHelper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 动态table切换 切面处理
 *
 * @author :zyf
 * @date:2020-04-25
 */
@Aspect
@Component
public class EventTrackingDynamicTableAspect {

    /**
     * 定义切面拦截切入点
     */
    @Pointcut("@annotation(org.jeecg.common.aspect.annotation.EtDynamicTable)")
    public void etDynamicTable() {
    }


    @Around("etDynamicTable()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        EtDynamicTable dynamicTable = method.getAnnotation(EtDynamicTable.class);
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        // 获取前端传递的项目名称
        String tableName = request.getParameter("project");
        if (StringUtils.isBlank(tableName)) {
            tableName = dynamicTable.value();
        }
        //获取前端传递的版本标记
        String version = request.getHeader(CommonConstant.VERSION);
        //存储版本号到本地线程变量
        ThreadLocalDataHelper.put(CommonConstant.VERSION, version);
        //存储表名到本地线程变量
        ThreadLocalDataHelper.put(CommonConstant.DYNAMIC_TABLE_NAME, tableName);
        //执行方法
        Object result = point.proceed();
        //清空本地变量
        ThreadLocalDataHelper.clear();
        return result;
    }

}
