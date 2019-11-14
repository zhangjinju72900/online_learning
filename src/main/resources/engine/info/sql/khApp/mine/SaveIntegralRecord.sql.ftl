INSERT INTO `t_customer_user_integral_record`
(customer_user_id, base_order_id, base_order_type, integral, change_type, change_time, create_time, create_by, update_time, update_by) 
VALUES (#{data.userId},#{data.baseId},0,#{data.integral},0,now(),now(),#{data.userId},now(),#{data.userId})