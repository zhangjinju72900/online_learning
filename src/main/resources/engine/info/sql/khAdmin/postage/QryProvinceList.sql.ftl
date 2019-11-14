select * from (
	select 
	name as text,
	code as value,
	code
 	from t_city 
 	where level = 1
) a 
