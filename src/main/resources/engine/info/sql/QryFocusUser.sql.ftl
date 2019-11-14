select 
	u.id,
	u.easemod_username easName,
	f.focus_on_id,
	f.customer_user_id 
from t_user_focus_on f LEFT JOIN t_customer_user u on u.id=f.customer_user_id where f.focus_on_id=#{data.userId}