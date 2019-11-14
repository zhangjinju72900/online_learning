select * from (
	select
	c.school_id as schoolId
	from t_class c
	where c.id = #{data.id} and valid_flag = 0
 ) a
