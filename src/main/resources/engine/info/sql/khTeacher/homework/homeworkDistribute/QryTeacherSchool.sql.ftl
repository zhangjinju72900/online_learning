select * from (
	select 
	school_id as schoolId
	from t_customer_user
	where id =  #{data.session.userInfo.userId}
 ) a 