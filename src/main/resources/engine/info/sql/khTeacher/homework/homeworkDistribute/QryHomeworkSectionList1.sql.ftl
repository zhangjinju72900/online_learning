select * from (
	select  
	s.id as value,
	s.name as text
	from t_section s
	
	where s.course_id=#{data.courseId} and valid_flag = 0
 ) a 