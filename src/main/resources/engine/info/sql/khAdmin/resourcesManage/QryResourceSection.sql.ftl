select * from (
	select id
	from t_course_section_resources
	where customer_resources_id=#{data.id}
 ) a 