SELECT
	a.id
FROM
	t_attend_class_record a
JOIN t_customer_user_class c ON c.class_id = a.class_id
WHERE
	c.customer_user_id = #{data.customerUserId}
AND a.valid_flag = 0
AND date_format(a.start_time, '%Y-%m') = date_format(
	DATE_SUB(curdate(), INTERVAL 1 MONTH),
	'%Y-%m'
)