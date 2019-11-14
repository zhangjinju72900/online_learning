SELECT
	cu.id as userId,
	#{data.id} AS id,
	cu.`name`,
	cu.nickname,
	case when cu.sex = 0 then '男' else '女' end as sex,
	cu.school_id as schoolId,
	s.`name` AS schoolName,
	tab1.classId,
	tab1.className,
	tab1.grade,
	cu.tel,
	CASE
		WHEN tab2.apply_by IS NULL THEN
			'0'
		ELSE
			'1'
		END AS isApply,
	CASE
		WHEN tab3.join_by IS NULL THEN
			'0'
		ELSE
			'1'
		END AS isJoin
FROM
	t_customer_user cu
LEFT JOIN t_school s ON cu.school_id = s.id
LEFT JOIN (
	SELECT
		cuc.customer_user_id,
		GROUP_CONCAT(cuc.class_id) AS classId,
		GROUP_CONCAT(c.`name`) AS className,
		GROUP_CONCAT(c.grade) AS grade
	FROM
		t_customer_user_class cuc
	LEFT JOIN t_class c ON cuc.class_id = c.id
	WHERE
		cuc.customer_user_id = #{data.userId}
) tab1 ON cu.id = tab1.customer_user_id
LEFT JOIN (
	SELECT
		apply_by
	FROM
		t_activity_apply
	WHERE
		activity_id = #{data.id}
	AND apply_by = #{data.userId}
	GROUP BY
		apply_by
) tab2 ON cu.id = tab2.apply_by
LEFT JOIN (
	SELECT
		join_by
	FROM
		t_activity_join
	WHERE
		activity_id = #{data.id}
	AND join_by = #{data.userId}
	GROUP BY
		join_by
) tab3 ON cu.id = tab3.join_by
where cu.id = #{data.userId}