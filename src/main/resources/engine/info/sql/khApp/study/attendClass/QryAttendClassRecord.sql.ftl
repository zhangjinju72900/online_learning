select id, attendClassDate, status as status from (
	select a.id,a.startTime,a.attendClassDate,case when sum(a.`status`)>0 then 1 else 0 end `status` from (select id, start_time as startTime, DATE_FORMAT(start_time, '%Y-%m-%d') as attendClassDate, case when student_count > real_student_count then 1 else 0 end as status
	 from t_attend_class_record where teacher_id = #{data.userId} and valid_flag = 0
		 and start_time >= #{data.startTime} AND start_time <= #{data.endTime}  and #{data.roleId} = '11')a GROUP BY attendClassDate
union all 	
		select b.id,b.startTime,b.attendClassDate,case when sum(b.`status`)=0 then 0 else 1 end status from (select r.id, r.start_time as startTime, DATE_FORMAT(r.start_time, '%Y-%m-%d') as attendClassDate, 
		(select case when count(1) = 0 then 1 else 0 end as status from t_attend_class_sign_record s where attend_class_record_id = r.id and s.valid_flag=0  and student_id = #{data.userId}) as status 
	from t_attend_class_record r join t_customer_user_class c
		on r.class_id = c.class_id 
	 	where c.customer_user_id = #{data.userId} and r.valid_flag = 0 and r.start_time >= #{data.startTime} AND r.start_time <= #{data.endTime}  and #{data.roleId} = '10' )b GROUP BY attendClassDate
	)a	group by attendClassDate