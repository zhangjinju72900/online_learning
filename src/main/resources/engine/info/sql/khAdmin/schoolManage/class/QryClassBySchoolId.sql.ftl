select * from (
	select
	count(*) as sum
	from t_class c
	where c.school_id=#{data.schoolId} and c.grade=#{data.grade} and c.name=#{data.name} and c.id != #{data.id}
 ) a
