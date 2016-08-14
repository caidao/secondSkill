package org.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillMapper;
import org.seckill.dao.SuccessKillMapper;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/1.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Resource
    private SeckillMapper seckillMapper;

    @Resource
    private SuccessKillMapper successKillMapper;

    @Resource
    private RedisDao redisDao;

    private final String slat="345533dddeet";


    public List<Seckill> getSeckillList() {
        return seckillMapper.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillMapper.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {

        //优化点：缓存优化
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null){
            seckill=seckillMapper.queryById(seckillId);
            if (seckill == null){
                return new Exposer(seckillId,false);
            }else {
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime =seckill.getEndTime();
        Date nowTime = new Date();

        if (nowTime.getTime()<startTime.getTime()||
                nowTime.getTime()>endTime.getTime()){
            return new Exposer(seckillId,false,nowTime.getTime(),
                    startTime.getTime(),endTime.getTime());
        }

        //转化特定字符串的过程
        String md5 = getMd5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    @Transactional
    /**
     * 使用注解控制事务的优点：
     * 1、开发团队达成一致约定，明确标注事务方法的编码风格
     * 2、保证事务方法的执行时间尽可能短，不要穿插其他网络操作，rpc/http请求或者剥离到事务方法外部
     * 3、不是所有方法都需要事务，
     */
    public SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException,RepeatKillException, SeckillCloseException {

        if (md5==null || !md5.equals(getMd5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        try {
            //记录购买行为
            int insertCount = successKillMapper.insertSuccessKilled(seckillId, userPhone);
            //唯一
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                //执行秒杀逻辑：减库存+记录购买行为

                //减库存，热点商品竞争
                int updateCount = seckillMapper.reduceNumber(seckillId, new Date());
                if (updateCount <= 0) {
                    //没有更新记录,秒杀结束
                    throw new SeckillCloseException("seckill is close");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKillMapper.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        } catch (Exception ex){
            logger.error(ex.getMessage(),ex);
            throw new SeckillException("secjill inner error:"+ex.getMessage());
        }

    }

    @Override
    public SeckillExecution excuteSeckillProducer(long seckillId, long userPhone, String md5) {
        if (md5==null || !md5.equals(getMd5(seckillId))){
            return new SeckillExecution(seckillId,SeckillStatEnum.DATA_REWRITE);
        }

        Date killTime = new Date();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        //执行存储过程
        try{
            seckillMapper.killByProcedure(map);
            int result =MapUtils.getInteger(map,"result",-2);
            if (result ==1){
                //秒杀成功
                SuccessKilled successKilled = successKillMapper.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            }else {
                return new SeckillExecution(seckillId,SeckillStatEnum.stateOf(result));
            }
        }catch (Exception ex){
            logger.error(ex.getMessage(),ex);
            return new SeckillExecution(seckillId,SeckillStatEnum.INNER_ERROR);
        }
    }

    private String getMd5(long seckillId){
        String base = seckillId+"/"+slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
