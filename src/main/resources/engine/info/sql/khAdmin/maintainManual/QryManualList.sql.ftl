select * from (

	select concat(parent_id,id),
	id as id,
    CASE
		WHEN parent_id = 0 THEN
			''
		ELSE
			parent_id
		END AS parentId,
	name as text,
	'closed' as state
	from t_maintain_manual
	where valid_flag=0 and data_flag='0' 

 ) a 