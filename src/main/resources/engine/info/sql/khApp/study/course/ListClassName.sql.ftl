SELECT distinct
	tab1.className AS text,
    tab1.class_id AS value
FROM
	t_curriculum_course cc
JOIN (
	SELECT
		c.curriculum_id,
		cuc.class_id,
		CONCAT(c.grade, '级', c.name) AS className,
		TIMESTAMPDIFF(YEAR, c.start_class_time, now()) AS yearDif
	FROM
		t_customer_user_class cuc
	JOIN t_class c ON cuc.class_id = c.id
	LEFT JOIN t_curriculum cu ON c.curriculum_id = cu.id
	WHERE
		customer_user_id = #{data.session.userInfo.userId}
	AND cu.valid_flag = 0
	AND c.valid_flag = 0
	AND cu.execute_time IS NOT NULL
	AND now() >= cu.execute_time
) tab1 ON cc.curriculum_id = tab1.curriculum_id
AND cc.grade_type = tab1.yearDif + 1
LEFT JOIN t_course cou ON cc.course_id = cou.id
WHERE
	cou.visible_flag = 0
AND cou.valid_flag = 0 order by cou.show_order