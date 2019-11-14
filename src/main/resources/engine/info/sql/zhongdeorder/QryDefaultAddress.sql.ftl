select * from (
	SELECT 
	id,
	name,
	tel,
	address,
	address_detail as addressDetail
	from t_consign_address
	where user_id = #{data.session.userInfo.userId} and is_default = '0' and valid_flag = '0'
) a 
