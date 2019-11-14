select * from (
	select 
	id as value,
	NAME as text 
	from t_region where valid_flag = 0
 ) a 