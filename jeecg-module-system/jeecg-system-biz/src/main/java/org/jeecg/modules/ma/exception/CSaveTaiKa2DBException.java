package org.jeecg.modules.ma.exception;

/**
 * 自定义保存商铺台卡店铺参数到数据库中异常
 * @author: xiaowei.song
 */
public class CSaveTaiKa2DBException extends Exception {

    private static final long serialVersionUID = 1639374111871115063L;

    public CSaveTaiKa2DBException(String message) {
        super(message);
    }
}
