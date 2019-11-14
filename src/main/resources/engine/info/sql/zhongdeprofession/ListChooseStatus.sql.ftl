select * from (
	select 
	CODE as value,
	NAME as text
	from t_dict where cata_code='t_course.visible_flag'
 ) a 