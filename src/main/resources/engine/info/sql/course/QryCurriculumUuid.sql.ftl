select * from (
	select 
	id as curriculumId 
	from t_curriculum
	where uuid = #{data.uuid}
 ) a 