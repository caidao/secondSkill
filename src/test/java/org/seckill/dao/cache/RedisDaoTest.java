package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillMapper;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/8/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:mybatis-context.xml"})
public class RedisDaoTest {

    private long id =1000L;
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private SeckillMapper seckillMapper;

    @Test
    public void testSeckill() throws Exception {

        Seckill seckill =redisDao.getSeckill(id);
        if (seckill == null){
            seckill =seckillMapper.queryById(id);
            if (seckill!=null) {
                redisDao.putSeckill(seckill);
                System.out.println(seckill);
                Seckill seckill1 = redisDao.getSeckill(id);
                System.out.println(seckill);
            }


        }

    }

}