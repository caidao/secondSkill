package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/8/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:mybatis-context.xml","classpath:spring-context.xml"})
public class SeckillServiceTest {

    private final Logger logger= LoggerFactory.getLogger(SeckillServiceTest.class);

    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() throws Exception {
        List<Seckill> list =seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void testGetById() throws Exception {
        Seckill seckill = seckillService.getById(1000);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void testExportSeckillUrl() throws Exception {
        long id=1000L;
        Exposer exposer=seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()){
            logger.info("exposer={}",exposer);
            SeckillExecution execution=seckillService.excuteSeckill(id,15677333L,exposer.getMd5());
            logger.info("execution={}",execution);
        }else {
            logger.warn("exposer={}",exposer);
        }

        //exposer=Exposer{exposed=true, md5='f6e6241b7edeb11f0f2defdeb2c2fb56', seckillId=1000, now=0, start=0, end=0}

    }

    @Test
    public void testExcuteSeckill() throws Exception {
        String md5="f6e6241b7edeb11f0f2defdeb2c2fb56";
        SeckillExecution execution=seckillService.excuteSeckill(1000L,15677333L,md5);
        logger.info("execution={}",execution);
    }

    @Test
    public void testExportSeckillUrlByProducer() throws Exception {
        long id=1000L;
        Exposer exposer=seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()){
            logger.info("exposer={}",exposer);
            SeckillExecution execution=seckillService.excuteSeckillProducer(id,15677333L,exposer.getMd5());
            logger.info("execution={}",execution);
        }else {
            logger.warn("exposer={}",exposer);
        }

        //exposer=Exposer{exposed=true, md5='f6e6241b7edeb11f0f2defdeb2c2fb56', seckillId=1000, now=0, start=0, end=0}

    }
}