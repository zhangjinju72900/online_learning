SELECT
	id
FROM
	t_attend_class_sign_record
WHERE
	student_id = #{data.customerUserId}
AND valid_flag = 0
AND date_format(sign_time, '%Y-%m') = date_format(
	DATE_SUB(curdate(), INTERVAL 1 MONTH),
	'%Y-%m'
)