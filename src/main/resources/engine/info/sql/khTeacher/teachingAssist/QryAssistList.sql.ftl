select * from(
	SELECT
	ta.id,
	ta.name,
	ta.professional_id as professionalId,
	ta.course_id as courseId,
	ta.section_id as sectionId,
	ta.visible_flag as visibleFlag,
	case when ta.visible_flag = 0 then '公开'
	else '不公开' end as visibleFlagName,
	case when ta.oss_key ='' or ta.oss_key is null then f.oss_key else ta.oss_key end ossKey,
	case when ta.bucket_name ='' or ta.bucket_name is null  then SUBSTR(f.oss_url FROM 8 FOR 11) else SUBSTR(ta.bucket_name FROM 8 FOR 11) end bucketName,
	case when ta.bucket_name ='' or ta.bucket_name is null  then SUBSTR(f.oss_url FROM 8 FOR 11) else SUBSTR(ta.bucket_name FROM 8 FOR 11) end ossUrl,
	p.name as professionalName,
	c.name as courseName,
	s.name as sectionName,
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
	where ta.teacher_id = #{data.session.userInfo.userId} and ta.valid_flag = 0
) a