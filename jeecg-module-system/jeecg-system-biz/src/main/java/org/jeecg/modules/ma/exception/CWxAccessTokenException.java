package org.jeecg.modules.ma.exception;

/**
 * 自定义获取access_token异常
 * @author: xiaowei.song
 */
public class CWxAccessTokenException extends Exception {

    private static final long serialVersionUID = 1639374111871115063L;

    public CWxAccessTokenException(String message) {
        super(message);
    }
}
