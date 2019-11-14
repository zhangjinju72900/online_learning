	select * from (
	select 
	CODE as value,
	NAME as text 
	from t_city where level='1'
    )a