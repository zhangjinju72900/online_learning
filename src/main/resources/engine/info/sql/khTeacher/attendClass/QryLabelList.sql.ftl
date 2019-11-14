SELECT 
	cl.label_id                  AS id,
	cl.course_id                 AS courseId,
	0 AS sectionId,
	'course' as type,
	l.name        AS text,
	l.show_order                 as showOrder,
	2                            as defaultOrder
	
	FROM t_course_label cl
	LEFT JOIN t_label l ON l.id = cl.label_id and l.valid_flag = 0
	left join t_label_role lr on l.id = lr.label_id
	where l.label_type = 0 and l.used_flag = 0
	AND cl.course_id = #{data.courseId}
	and lr.role_id = 11

UNION ALL

	SELECT
		sl.label_id AS id,
		s.course_id AS courseId,
		sl.section_id AS sectionId,
		'section' as type,
		la.NAME AS text,
		la.show_order AS showOrder,
		3 AS defaultOrder
	FROM
		t_section_label sl
	LEFT JOIN t_section s ON sl.section_id = s.id
	LEFT JOIN t_label la ON sl.label_id = la.id 
	left join t_label_role lr on la.id = lr.label_id
	where la.valid_flag = 0 and sl.section_id = #{data.sectionId}
	AND s.valid_flag = 0
	and la.label_type = 1 and la.used_flag = 0 and lr.role_id = 11

UNION ALL

	SELECT
		0 as id,
		#{data.courseId} as courseId,
		#{data.sectionId} as sectionId,
		'teachingAssist' as type,
		'我的资料' as text,
		9 as showOrder,
		1 AS defaultOrder
	FROM
		DUAL
	WHERE
		EXISTS (
			SELECT
				id
			FROM
				t_teaching_assist
			WHERE
				course_id = #{data.courseId}
			AND section_id = #{data.sectionId}
			AND teacher_id = #{data.session.userInfo.userId}
		)

ORDER BY defaultOrder desc, showOrder asc
