select * from (
	select 
	id as value,
	title as text
	from t_subject where valid_flag = 0
 ) a 