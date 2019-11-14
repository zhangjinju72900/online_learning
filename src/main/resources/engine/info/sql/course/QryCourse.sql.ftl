select * from(
	SELECT 
		curr.id,
		curr.name as courseName,
		curr.create_time as createTime,
		curr.execute_time as executeTime,
		curr.create_by as createBy,
		curr.update_time as updateTime,
		curr.update_by as updateBy,
		tab1.className as className,
	    r.name as regionName,
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
		LEFT JOIN t_region r on r.id=s.region_id
		WHERE curr.name LIKE CONCAT('%',#{data.courseName},'%')
		AND ifnull(s.name,'') LIKE CONCAT('%',#{data.schoolName},'%')
		AND curr.valid_flag = 0
) tab