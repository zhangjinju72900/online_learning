select * from(
	select 
	id as userId
	from t_customer_user 
	where uuid =#{data.uuid}
)a