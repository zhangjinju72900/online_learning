select * from (
	SELECT 
	u.password as password,
	right(password,32) as salt
	from t_customer_user u
	where id=#{data.userId}
) a 