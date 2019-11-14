select * from (	
	SELECT
		p.id as id,
		p.good_id as goodId,
		p.integral as integral,
		p.amount as amount
	FROM t_goods_pay_detail p
	WHERE p.good_id = #{data.id}
) tab