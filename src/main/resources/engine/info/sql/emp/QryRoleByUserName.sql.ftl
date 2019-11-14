select GROUP_CONCAT(r.role_id)as roleId from t_customer_user u LEFT JOIN t_customer_user_role r on r.customer_user_id=u.id 
where u.card_num=#{data.userName} or u.tel=#{data.userName}  GROUP BY u.id