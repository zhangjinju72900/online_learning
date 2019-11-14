select * from(
	select t.id,
	t.name,
	t.professional_id as professionalId,
	t.course_id as courseId,
	t.section_id as sectionId,
	t.visible_flag as visibleFlag,
	case t.visible_flag when 0 then '公开'
	else '不公开' end as visibleFlagName,
	t.file_id as fileId,
	t.oss_key as ossKey,
	t.bucket_name as bucketName,
	s.name as sectionName,
	c.name as courseName,
	p.name as professionalName,
	f.filename as fileName
	FROM t_teaching_assist t
	LEFT JOIN t_professional p ON t.professional_id = p.id
	LEFT JOIN t_course c ON t.course_id = c.id
	left join t_section s on t.section_id = s.id
LEFT JOIN t_file_index f on f.id=t.file_id
) a