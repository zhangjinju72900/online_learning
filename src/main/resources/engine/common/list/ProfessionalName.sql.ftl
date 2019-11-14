 select * from (
	select 
	DISTINCT
	tp.id as value,
	tp.name as text
	from t_professional tp
 ) a 