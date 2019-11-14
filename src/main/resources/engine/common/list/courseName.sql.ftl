select * from (
	select 
	distinct th.course_id as value,
	tc.name as text
	from t_homework th
	LEFT JOIN t_course tc ON tc.id = th.course_id where th.class_id = #{data.classId}
 ) a 