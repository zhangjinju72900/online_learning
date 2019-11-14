SELECT 
		csr.id            as id,
		csr.customer_resources_id as customerResourcesId,
		csr.id  as csrId,
		case csr.course_resources_name
			when '' then cr.name
				else csr.course_resources_name
			end             as courseResName,
		cr.oss_key as ossKey,
		cr.bucket_name as bucketName,
		cr.resources_type as resourcesType,
		cr.file_path as filePath,
		csr.section_id as sectionId ,
		cr.file_type as fileType	
	FROM t_course_section_resources csr
	LEFT JOIN t_customer_resources cr ON csr.customer_resources_id = cr.id
	LEFT JOIN t_label l ON l.id = csr.label_id
where cr.backup_type = '1'
	and cr.valid_flag = '0'
	and cr.file_type != ''
	and l.id = #{data.id}
	and csr.course_id = #{data.courseId}
	and csr.section_id = #{data.sectionId}
	
UNION ALL

	select 
		t.id,
		t.id as customerResourcesId,
		t.id as csrId,
		t.name as courseResName,
		t.oss_key as ossKey,
		t.bucket_name as bucketName,
		0 as resourcesType,
		"" as filePath,
		t.section_id as sectionId,
		f.file_type as fileType
	from t_teaching_assist t
	LEFT JOIN t_file_index f on f.id = t.file_id
	 where #{data.id} = '0' and t.teacher_id = #{data.session.userInfo.userId} 
	 and t.course_id = #{data.courseId} and t.section_id = #{data.sectionId} and t.visible_flag = 0 