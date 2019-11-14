SELECT 
	l.id as id,
	GROUP_CONCAT(csr.customer_resources_id) as resourcesIds,
	l.name as text,
	GROUP_CONCAT(case csr.course_resources_name
		when '' then cr.name
		else csr.course_resources_name
		end)  as courseResNames,
	GROUP_CONCAT(cr.oss_key) as ossKeys,
	GROUP_CONCAT(cr.bucket_name) as bucketNames,
	GROUP_CONCAT(cr.resources_type) as resourcesType,
	GROUP_CONCAT(cr.file_type) as fileType,
	GROUP_CONCAT(cr.file_path) as filePath,
	GROUP_CONCAT(csr.section_id) as sectionId,
	GROUP_CONCAT(csr.label_id) as labelId
FROM t_course_section_resources csr
LEFT JOIN t_customer_resources cr ON csr.customer_resources_id = cr.id
LEFT JOIN t_label l ON l.id = csr.label_id
left join t_label_role lr on l.id = lr.label_id
where cr.backup_type = '1'
and cr.valid_flag = '0'
and cr.file_type != ''
and l.valid_flag = '0' and l.used_flag = '1' and(section_id = 0 or section_id = #{data.sectionId})
and csr.course_id = #{data.courseId} 
and lr.role_id = 11 group by l.id