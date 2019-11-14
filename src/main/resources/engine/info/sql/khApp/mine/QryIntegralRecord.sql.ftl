select * from (
	SELECT 
	t.id,
	t.customer_user_id as customerUserId,
	t.base_order_id as baseOrderId,
	t.base_order_type as baseOrderType,
	t.integral,
	t.change_type as changeType,
	case when (t.change_type = 10 or t.change_type=8) then t.remark 
	else d.name end as changeTypeName,
	t.change_time as changeTime
    from t_customer_user_integral_record t
    LEFT JOIN t_dict d ON t.change_type=d.code AND d.cata_code='integral_record.type'
) a 