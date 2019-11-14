select 
	u.id,
	u.`name`,
	u.nickname,
	#{data.salt} as salt
from t_customer_user u where u.id=#{data.session.userInfo.userId}