insert into t_customer_user_integral_record (
	base_order_id,
  base_order_type,
change_time,
change_type,
create_by,
create_time,
customer_user_id,
integral,
remark,
update_by,
update_time
)
VALUES
(
	8,
	8,
	NOW(),
	8,
	#{data.userId},
	NOW(),
	#{data.userId},
	#{data.integeral},
  #{data.remark},
#{data.userId},
	NOW()
)