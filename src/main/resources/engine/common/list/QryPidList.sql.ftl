select * from (
	select 
	id as value,
	name as text
	from t_professional
	where valid_flag=0

union ALL

	select 
	
	c.id as value,
	c.name as text 
	from  t_course c
	LEFT JOIN t_professional p ON c.professional_id=p.id 
	LEFT JOIN  t_customer_user tcu1 ON p.update_by=tcu1.id 
	LEFT JOIN  t_customer_user tcu2 ON p.create_by=tcu2.id 
	LEFT JOIN t_file_index f ON f.id=c.file_id
	where c.valid_flag=0

UNION all
 SELECT
		s.id AS value,
		CONCAT('小节-', s.name) AS text
		
	FROM t_section s WHERE   s.valid_flag=0
 ) a 

