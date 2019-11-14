select *
from (
SELECT c.id   AS value,
c.name AS text
FROM t_customer_user_class cuc
LEFT JOIN t_class c ON c.id = cuc.class_id
WHERE cuc.customer_user_id = #{data.session.userInfo.userId}
) a;