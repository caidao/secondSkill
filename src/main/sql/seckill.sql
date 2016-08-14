

--定义存储过程
--row_count():返回上一条修改类型sql的影响行数
--row_count: 0未修改数据 >0 表示修改的行数：<0 sql错误/未执行修改sql
create PROCEDURE `seckill`.`execute_seckill`(in v_seckill_id bigint,
                    in v_phone bigint,
                    in v_kill_time TIMESTAMP ,out r_result int)
    BEGIN
      DECLARE  insert_count int DEFAULT 0;
      start TRANSACTION ;
      insert ignore into success_killed(seckill_id,user_phone,create_time)
      VALUES (v_seckill_id,v_phone,v_kill_time);
      select ROW_COUNT () into insert_count;
      if(insert_count=0)THEN
        ROLLBACK ;
        set r_result=-1;
      elseif(insert_count <0) THEN
        ROLLBACK ;
        set r_result =-2;
      ELSE
        update seckill set number =number -1
        where seckill_id =v_seckill_id
              and seckill_id =v_seckill_id
              and end_time> v_kill_time
              and start_time < v_kill_time
              and number >0;
        select ROW_COUNT () into insert_count;
        if(insert_count=0) THEN
          ROLLBACK ;
          set r_result=0;
        ELSEif(insert_count <0) THEN
          ROLLBACK ;
          set r_result=-1;
        ELSE
          commit;
          set r_result=1;
        end if;
      end if;
    end;
--定义存储过程结束

delimiter;
set @r_result=-3;
--执行存储过程
call execute_seckill(1003,1585691594,now(),@r_result);
--获取结果
select @r_result;

-- 1、存储过程优化：事务行级锁持有的时间
-- 2 、不要过度依赖存储过程
-- 3、简单的逻辑可以使用存储过程
-- 4、qps：一个秒杀单6000/qps











