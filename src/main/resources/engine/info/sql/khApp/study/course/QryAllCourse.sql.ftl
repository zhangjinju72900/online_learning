select * from(
SELECT
	cou.id,cou.`name`,cou.course_count AS courseCount,cou.difficulty_level AS difficultyLevel,cou.file_id,cou.remark,tab1.className, tab1.classId
FROM
	t_curriculum_course cc
JOIN (
	SELECT
		c.curriculum_id,
		cuc.class_id as classId,
		c.name AS className,
		TIMESTAMPDIFF(YEAR, c.start_class_time, now()) AS yearDif
	FROM
		t_customer_user_class cuc
	JOIN t_class c ON cuc.class_id = c.id
	LEFT JOIN t_curriculum cu ON c.curriculum_id = cu.id
	WHERE
		customer_user_id = #{data.session.userInfo.userId}
	AND cu.valid_flag = 0
	AND cu.execute_time IS NOT NULL
	AND now() >= cu.execute_time
) tab1 ON cc.curriculum_id = tab1.curriculum_id
AND tab1.yearDif + 1 >= cc.grade_type 
LEFT JOIN t_course cou ON cc.course_id = cou.id
WHERE
	cou.visible_flag = 0
AND cou.valid_flag = 0 order by cou.show_order
) a