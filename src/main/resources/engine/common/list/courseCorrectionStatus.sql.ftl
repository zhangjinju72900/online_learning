select * from (
	select 
	CODE as value,
	NAME as text from t_dict WHERE cata_code='t_course_resources_error_recovery.status' order by seq
 ) a 