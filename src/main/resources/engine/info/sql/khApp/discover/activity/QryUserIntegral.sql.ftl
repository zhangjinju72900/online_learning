SELECT * FROM(
	SELECT integral
	FROM t_customer_user
	WHERE id=#{data.userId}
)a