package org.jeecg.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * @author xiaowei.song
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncTaskConfig implements AsyncConfigurer {
    // ThredPoolTaskExcutor的处理流程
    // 当池子大小小于corePoolSize，就新建线程，并处理请求
    // 当池子大小等于corePoolSize，把请求放入workQueue中，池子里的空闲线程就去workQueue中取任务并处理
    // 当workQueue放不下任务时，就新建线程入池，并处理请求，如果池子大小撑到了maximumPoolSize，就用RejectedExecutionHandler来做拒绝处理
    // 当池子的线程数大于corePoolSize时，多余的线程会等待keepAliveTime长时间，如果无请求可处理就自行销毁

    @Override
    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        //设置核心线程数
        threadPool.setCorePoolSize(2);
        //设置最大线程数
        threadPool.setMaxPoolSize(5);
        //线程池所使用的缓冲队列
        threadPool.setQueueCapacity(10);
        //等待任务在关机时完成--表明等待所有线程执行完
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        threadPool.setAwaitTerminationSeconds(60);
        //  线程名称前缀
        threadPool.setThreadNamePrefix("Xingpan-Async-");
        // 初始化线程
        threadPool.initialize();
        return threadPool;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            /**
             * Handle the given uncaught exception thrown from an asynchronous method.
             *
             * @param ex     the exception thrown from the asynchronous method
             * @param method the asynchronous method
             * @param params the parameters used to invoked the method
             */
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                log.error("异步任务执行异常，方法为: {}, 参数为: {}, 异常信息为: ", method, params, ex);
            }
        };
    }
}