select * from (
	select concat(parent_id,id),
	id as id,
	CASE
		WHEN parent_id = 0 THEN
			''
		ELSE
			parent_id
		END AS pid,
	name as text,
	'closed' as state
	from t_professional
	where valid_flag=0
ORDER BY concat(parent_id,id)
 ) a 

