select * from (
	select 
	id as id,
	name as name
	 from t_curriculum where school_id = #{data.id} and valid_flag = '0'
 ) a 