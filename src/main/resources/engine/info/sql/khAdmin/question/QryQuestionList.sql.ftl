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

union ALL

	select 
	concat(c.professional_id,c.id),
	c.id as id,
	c.professional_id as pid,
	c.name as text ,
'closed' as state
	from  t_course c
	LEFT JOIN t_professional p ON c.professional_id=p.id 
	LEFT JOIN  t_customer_user tcu1 ON p.update_by=tcu1.id 
	LEFT JOIN  t_customer_user tcu2 ON p.create_by=tcu2.id 
	LEFT JOIN t_file_index f ON f.id=c.file_id
	where c.valid_flag=0
 ) a 

