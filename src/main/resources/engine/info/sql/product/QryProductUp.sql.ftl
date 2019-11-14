UPDATE 
	t_goods g
SET sale_status = 0
WHERE g.id = #{data.id};