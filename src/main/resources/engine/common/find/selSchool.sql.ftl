select * from (
	select 
	id as id,
	name as name
	 from t_school where valid_flag = '0'
 ) a 