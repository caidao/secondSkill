package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/8/10.
 */
public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(RedisDao.class);
    private JedisPool jedisPool;

    public RedisDao(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }

    public Seckill getSeckill(long seckillId){
        try{
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:"+seckillId;
                //采用自定义序列化
                byte[] bytes = jedis.get(key.getBytes());
                //缓存中获取到
                if (bytes != null){
                    Seckill seckill =schema.newMessage();
                    //性能更好的序列化
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    return seckill;
                }
            }finally {
                jedis.close();
            }
        }catch (Exception ex){

        }
        return null;
    }

    public String  putSeckill(Seckill seckill){
        try{
            Jedis  jedis = jedisPool.getResource();
            try {
                String key = "seckill:"+seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill,schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                jedis.setex(key.getBytes(),60*60,bytes);
            }finally {
                jedis.close();
            }
        }catch (Exception ex){
            logger.error(ex.getMessage(),ex);
        }
        return null;
    }

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
}
