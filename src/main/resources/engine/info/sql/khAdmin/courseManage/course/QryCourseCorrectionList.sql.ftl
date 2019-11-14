select * from(
SELECT
	tcre.id AS id,
	tcre.course_id AS courseId,
	tc.name AS courseName,
	tcre.section_id AS sectionId,
	ts.name AS sectionName,
	l.`name` AS labelId,
	tcre.customer_resources_id AS customerResourcesId,
	tcre.course_resources_name AS courseResourcesName,
	tcre.file_id AS fileId,
	tcre.oss_key AS ossKey,
	tcre.oss_url AS ossUrl,
	tcre.content AS content,
	tcre.status AS status,
	td.name AS statusName,
	tcre.school_id AS schoolId,
	s.name AS schoolName,
	tcre.teacher_id AS teacherId,
	tcu.name AS teacherName,
	tcre.valid_flag AS validFlag,
	tcre.create_time AS createTime,
	tcre.create_by AS createBy,
	tcre.update_time AS updateTime,
	tcre.update_by AS updateBy
FROM
	t_course_resources_error_recovery tcre 
LEFT JOIN t_course tc ON tc.id = tcre.course_id 
LEFT JOIN t_section ts ON ts.id = tcre.section_id 
LEFT JOIN t_school s ON s.id = tcre.school_id 
LEFT JOIN t_customer_user tcu ON tcu.id = tcre.teacher_id
LEFT JOIN t_label l on l.id=tcre.label_id 
LEFT JOIN t_dict td ON td.code = tcre.status AND td.cata_code = 't_course_resources_error_recovery.status' 
WHERE tcre.valid_flag = 0 
ORDER BY updateTime DESC
)a