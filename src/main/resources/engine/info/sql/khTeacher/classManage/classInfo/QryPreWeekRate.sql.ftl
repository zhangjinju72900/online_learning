SELECT
	class_id as classId, SUM(sign_in_rate) rate
FROM
	t_class_sign_rate
WHERE
	YEARWEEK(
		date_format(data_date, '%Y-%m-%d')
	) = YEARWEEK(now()) - 1 GROUP BY class_id ORDER BY SUM(sign_in_rate) desc