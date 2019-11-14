SELECT
	class_id as classId, SUM(sign_in_rate) rate
FROM
	t_class_sign_rate
WHERE
	date_format(data_date, '%Y-%m') = date_format(
		DATE_SUB(curdate(), INTERVAL 1 MONTH),
		'%Y-%m'
	) GROUP BY class_id ORDER BY SUM(sign_in_rate) desc
