select * from (
	select  
	c.name  as  provinceName
 	from  t_city c
 	where c.code=#{data.province}
) a 
