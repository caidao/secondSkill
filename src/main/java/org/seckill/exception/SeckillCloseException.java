package org.seckill.exception;

import org.seckill.dto.SeckillExecution;
import org.seckill.entity.SuccessKilled;

/**
 * 秒杀关闭异常
 * Created by Administrator on 2016/8/1.
 */
public class SeckillCloseException extends SeckillException{


    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeckillCloseException(String message) {
        super(message);
    }
}
