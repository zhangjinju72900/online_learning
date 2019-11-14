select * from (
	SELECT DISTINCT
	DATE(distribute_time) as homeworkDate
	from t_homework
	where  distribute_status  in (1,2,3) and valid_flag = 0
	and distribute_time >= #{data.beginTime}
	and distribute_time <=  #{data.endTime}
	and create_by = #{data.teacherId}
 ) a 