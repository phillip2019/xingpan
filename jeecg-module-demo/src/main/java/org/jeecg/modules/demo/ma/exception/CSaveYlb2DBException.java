package org.jeecg.modules.demo.ma.exception;

/**
 * 自定义保存易拉宝参数到数据库中异常
 * @author: xiaowei.song
 */
public class CSaveYlb2DBException extends Exception {

    private static final long serialVersionUID = 1639374111871115063L;

    public CSaveYlb2DBException(String message) {
        super(message);
    }
}
