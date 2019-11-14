select * from (
	select  
	t.id  as id, 
	t.name  as name
	from  t_professional  t
	where t.valid_flag=0
) a 
