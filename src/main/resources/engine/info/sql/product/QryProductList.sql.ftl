select * from (	
	SELECT 
		g.id as id,
		g.name as name,
		g.code as code,
		g.sale_status as saleS,
		CASE 
			WHEN g.sale_status = '0' THEN '上架'
			WHEN g.sale_status = '1' THEN '下架'
		END as saleStatus,
		g.content as content,
		g.quantity as quantity
	FROM t_goods g
	where g.valid_flag=0
) tab