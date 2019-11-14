select * from(
	select id,
	name,
	professional_id as professionalId,
	course_id as courseId,
	section_id as sectionId,
	visible_flag as visibleFlag,
	file_id as fileId,
	oss_key as ossKey,
	bucket_name as bucketName
	FROM t_teaching_assist 
) a