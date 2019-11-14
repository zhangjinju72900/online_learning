	SELECT 
	id,
	user_id as userId
	from t_consign_address
    where id=#{data.id}