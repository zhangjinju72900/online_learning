UPDATE 
	t_goods g
SET sale_status = 1
WHERE g.id = #{data.id};