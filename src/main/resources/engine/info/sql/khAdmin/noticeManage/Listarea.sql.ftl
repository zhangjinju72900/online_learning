	select * from (
	select 
	id as value,
	name as text 
	from t_region where valid_flag = 0
    )a