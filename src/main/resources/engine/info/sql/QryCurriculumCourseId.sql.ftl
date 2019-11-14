select * from (
	select id as id,
	name as courseName,
	professional_id as pId
	from t_course where valid_flag = '0'
)a
