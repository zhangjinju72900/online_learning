select * from (
	select  
	p.id  as  id,
	p.province as province,
	c.name  as  provinceName,
	p.freeshipping as freeshipping,
	p.price  as  price
 	from t_postage p 
 	left join t_city c
	on p.province = c.code 
 	
) a 
