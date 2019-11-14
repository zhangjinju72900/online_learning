select * from(
	SELECT
	ta.id,
	ta.name,
	ta.professional_id as professionalId,
	ta.course_id as courseId,
	ta.section_id as sectionId,
	ta.visible_flag as visibleFlag,
	ta.teacher_id as teacherId,
	ta.create_time as createTime,
	ta.update_time as updateTime,
	case when ta.visible_flag = 0 then '公开'
	else '不公开' end as visibleFlagName,
	ta.oss_key as ossKey,
	ta.bucket_name  as bucketName,
	p.name as professionalName,
	c.name as courseName,
	s.name as sectionName,
	cu.name as teacherName,
	    r.name as regionName,
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
LEFT JOIN t_school ts on ts.id=cu.school_id
LEFT JOIN t_region r on r.id=ts.region_id
	where ta.valid_flag = 0
) a