-- 秒杀执行存储过程
DELIMITER $$ -- console; 转换为 $$
-- 定义存储过程
-- 参数：in 输入参数；out 输出参数
-- row_count()返回上一条修改类型sql（delete、insert、update）的影响行数
-- row_count():0未修改数据 >0表示修改的行数 <0sql错误或者未执行sql

CREATE PROCEDURE `seckill`.`execute_seckill`(
	IN v_seckill_id BIGINT ,
	IN v_phone BIGINT ,
	IN v_kill_time TIMESTAMP ,
	OUT r_result INT
)
BEGIN

	DECLARE insert_count int DEFAULT 0 ;
	START TRANSACTION ;
	INSERT IGNORE INTO success_killed(seckill_id ,user_phone ,create_time)
	VALUES(v_seckill_id ,v_phone ,v_kill_time);
	SELECT ROW_COUNT() INTO insert_count;
	IF(insert_count = 0) THEN
		ROLLBACK;
		SET r_result = 0;
	ELSEIF(insert_count < 0) THEN
		ROLLBACK;
		SET r_result = -1;
	ELSE
		UPDATE seckill SET number = number -1
		WHERE seckill_id = v_seckill_id
		AND (v_kill_time BETWEEN start_time AND end_time)
		AND number > 0;
		SELECT ROW_COUNT() INTO insert_count ;
		IF(insert_count = 0) THEN
			ROLLBACK;
			SET r_result = 0;
		ELSEIF(insert_count < 0) THEN
			ROLLBACK;
			SET r_result = -1;
		ELSE
			COMMIT;
			SET r_result = 1;
		END IF;

	END IF;
END $$

-- 储存过程结束

show create procedure execute_seckill

DELIMITER ;
set @r_result=-3;
call execute_seckill(1001,13502178891,now(),@r_result);
SELECT @r_result;

--储存过程优化：事务行级锁持有的时间