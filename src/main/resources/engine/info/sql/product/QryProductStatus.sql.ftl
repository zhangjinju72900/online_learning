select * from (	
	SELECT
	code as value,
	name as text
	FROM t_dict 
	where cata_code='t_goods.sale_status'
) tab