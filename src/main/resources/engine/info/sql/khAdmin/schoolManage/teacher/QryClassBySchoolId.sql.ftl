select * from (
	select
	c.name 
	from t_class c
	where c.school_id=#{data.schoolId}
 ) a
