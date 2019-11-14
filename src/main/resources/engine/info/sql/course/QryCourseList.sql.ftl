select * from(
	SELECT 
		curr.id,
		curr.name as courseName,
		curr.create_time as createTime,
		curr.execute_time as executeTime,
		tab1.className as className,
		s.name as schoolName
		FROM t_curriculum curr
		LEFT JOIN (
			SELECT cl.curriculum_id, 
			GROUP_CONCAT(cl.id) as classId, 
			GROUP_CONCAT(cl.name) as className
		FROM
	 		t_class cl
			GROUP BY cl.curriculum_id
			) tab1
		ON curr.id = tab1.curriculum_id
		LEFT JOIN t_school s
		ON curr.school_id = s.id
WHERE curr.valid_flag = 0
) tab