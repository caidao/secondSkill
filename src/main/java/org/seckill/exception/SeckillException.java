package org.seckill.exception;

/**
 * 秒杀异常
 * Created by Administrator on 2016/8/1.
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeckillException(String message) {
        super(message);
    }
}
