select * from (
	SELECT
		g.id AS id,
	CASE
		WHEN g.sale_status = '0' THEN '上架'
		WHEN g.sale_status = '1' THEN '下架'
	END AS saleStatus
	FROM t_goods g
	WHERE g.id = #{data.eq_id}
) tab