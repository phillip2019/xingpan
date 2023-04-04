package org.jeecg.modules.ma.exception;

/**
 * 自定义获取微信公众号带参二维码异常
 * @author: xiaowei.song
 */
public class CWxQrCodeException extends Exception {

    private static final long serialVersionUID = 1639374111871115063L;

    public CWxQrCodeException(String message) {
        super(message);
    }
}
