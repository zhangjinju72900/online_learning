select 
tcuc.class_id    AS classId,
tcuc.customer_user_id   AS studentId,
tcu.name AS name
from t_customer_user_class tcuc
 join t_customer_user_role tcur ON tcuc.customer_user_id = tcur.customer_user_id 
left join t_customer_user tcu ON tcuc.customer_user_id = tcu.id
where tcuc.class_id = #{data.classId} AND tcur.role_id = 10 and tcu.valid_flag = 0 GROUP BY tcuc.customer_user_id
