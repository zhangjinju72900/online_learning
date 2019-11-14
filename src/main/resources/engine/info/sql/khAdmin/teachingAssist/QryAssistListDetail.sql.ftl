select * from(
	SELECT
	ta.id,
	ta.name,
	ta.professional_id as professionalId,
	ta.course_id as courseId,
	ta.section_id as sectionId,
	ta.visible_flag as visibleFlag,
	ta.teacher_id as teacherId,
	cu.create_time as createTime,
	cu.update_time as updateTime,
	cu1.name as createName,
	cu2.name as updateName,
	case when ta.visible_flag = 0 then '公开'
	else '不公开' end as visibleFlagName,
	ta.file_id as fileId,
	ta.oss_key as ossKey,
	ta.bucket_name as bucketName,
	p.name as professionalName,
	c.name as courseName,
	s.name as sectionName,
	cu.name as teacherName,
	f.file_type as fileType
	from t_teaching_assist ta 
	left join t_professional p
	on p.id = ta.professional_id  
	left join t_course c
	on c.id = ta.course_id
	left join t_section s
	on s.id = ta.section_id
	left join t_file_index f
	on f.id = ta.file_id
	left join t_customer_user cu 
	on cu.id  = ta.teacher_id
	left join t_customer_user cu1
	on cu1.id = ta.create_by
	left join t_customer_user cu2
	on cu2.id = ta.update_by
	where ta.valid_flag = 0
) a