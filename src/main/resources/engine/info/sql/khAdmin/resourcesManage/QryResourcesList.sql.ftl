select * from (
	select concat(parent_id,id),
	id as id,
	CASE
		WHEN parent_id = 0 THEN
			''
		ELSE
			parent_id
		END AS pid,
	name as text
	from t_customer_resources
	where backup_type = '1' and file_type = '' and valid_flag=0
ORDER BY concat(pid,id)
 ) a 