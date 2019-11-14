SELECT
	cou.id as value, cou.`name` as text
FROM
	t_curriculum_course cc
JOIN (
	SELECT
		c.curriculum_id,
		c.id as classId,
		c.name AS className,
		TIMESTAMPDIFF(YEAR, c.start_class_time, now()) AS yearDif
	FROM
		t_class c
	LEFT JOIN t_curriculum cu ON c.curriculum_id = cu.id
	WHERE
		c.id = #{data.classId}
	AND cu.valid_flag = 0
	AND cu.execute_time IS NOT NULL
	AND now() >= cu.execute_time
) tab1 ON cc.curriculum_id = tab1.curriculum_id
AND cc.grade_type = tab1.yearDif + 1
LEFT JOIN t_course cou ON cc.course_id = cou.id
WHERE
	cou.visible_flag = 0
AND cou.valid_flag = 0 order by cou.show_order