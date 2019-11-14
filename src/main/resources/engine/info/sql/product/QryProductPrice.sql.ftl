select * from (
	SELECT
		g.id as id,
		p.integral as integral,
		p.amount as amount
	FROM t_goods g
	LEFT JOIN t_goods_pay_detail p
	ON g.id = p.good_id
	WHERE g.id = #{data.id}
)a