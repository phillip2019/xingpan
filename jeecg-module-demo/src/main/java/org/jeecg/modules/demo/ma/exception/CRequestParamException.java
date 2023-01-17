package org.jeecg.modules.demo.ma.exception;

/**
 * 自定义请求参数异常
 * @author: xiaowei.song
 */
public class CRequestParamException extends Exception {

    private static final long serialVersionUID = 1639374111871115063L;

    public CRequestParamException(String message) {
        super(message);
    }
}
