select * from(
	select id as courseId
	from t_course 
	where professional_id = #{data.proId}
) tab