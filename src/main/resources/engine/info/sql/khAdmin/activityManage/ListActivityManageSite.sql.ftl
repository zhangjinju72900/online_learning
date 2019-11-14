select * from (
	select 
	code as value,
	name as text
	from t_city where level = 1
 ) a 