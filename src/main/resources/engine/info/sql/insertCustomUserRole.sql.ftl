insert into t_customer_user_role (customer_user_id,role_id,update_time,create_time) values 
(#{data.userId} ,#{data.roleId} ,now(),now()) 