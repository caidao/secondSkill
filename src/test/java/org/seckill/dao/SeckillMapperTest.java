package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/8/1.
 * 配置spring 和 junit整合，junit启动时加载springIOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:mybatis-context.xml"})
public class SeckillMapperTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillMapper seckillMapper;

    @Test
    public void testReduceNumber() throws Exception {
        Date killTime = new Date();
        int updateCount = seckillMapper.reduceNumber(1000L,killTime);
        System.out.println("updateCount="+updateCount);
    }

    @Test
    public void testQueryById() throws Exception {

        long id = 1000;
        Seckill seckill=seckillMapper.queryById(id);
        System.out.println(seckill);
    }

    @Test
    public void testQueryAll() throws Exception {
        List<Seckill> seckillList =seckillMapper.queryAll(0,100);
        for (Seckill seckill:seckillList){
            System.out.println(seckill);
        }
    }
}