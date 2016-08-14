package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/8/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:mybatis-context.xml"})
public class SuccessKillMapperTest {

    @Resource
    private  SuccessKillMapper successKillMapper;

    @Test
    public void testInsertSuccessKilled() throws Exception {
        int insertCount=successKillMapper.insertSuccessKilled(1001L,15856915194L);
        System.out.println("insertCount="+insertCount);


    }

    @Test
    public void testQueryByIdWithSeckill() throws Exception {
        SuccessKilled successKilled=successKillMapper.queryByIdWithSeckill(1001L,15856915194L);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}