select * from (
	select 
	DISTINCT
	ts.id as value,
	ts.name as text
	from t_section ts
	where ts.course_id=#{data.eq_courseId}
 ) a 