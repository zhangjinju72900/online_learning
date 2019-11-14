select * from (
	select 
	count(*) as sum
	 from t_curriculum where school_id = #{data.schoolId} and valid_flag = '0' and id = #{data.curriculumId}
 ) a 