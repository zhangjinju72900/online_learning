select * from (
	select 
	DISTINCT
	th.section_id as value,
	ts.name as text 
	from t_homework th 
	JOIN t_section ts ON ts.id = th.section_id where th.course_id = #{data.courseId}
 ) a 