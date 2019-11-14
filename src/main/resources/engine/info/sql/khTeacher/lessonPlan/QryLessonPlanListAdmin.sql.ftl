select * from (
SELECT 
	DISTINCT tlp.id AS id,
	tlp.name AS name,
	tlp.class_id AS classId,
	CONCAT(tc.grade,"级",tc.name) AS className,
	tlp.course_id AS courseId,
	tce.name AS courseName,
	tlp.section_id AS sectionId,
	ts.name AS sectionName,
	tlp.course_count AS courseCount,
	tlp.student_count AS studentCount,
	tlp.guide_teacher_id AS guideTeacherId,
	tcu.name AS guideTeacherName,
	tlp.teaching_object AS teachingObject,
	tlp.teaching_site AS teachingSite,
	tlp.equipment AS equipment,
	tlp.customer_task AS customerTask,
	tlp.teaching_goal AS teachingGoal,
	tlp.teaching_content AS teachingContent,
	tlp.homework_content AS homeworkContent,
	tlp.teacher_id AS teacherId,
	tlp.create_time AS createTime,
	r.name as regionName,	
	tcu.name AS createBy,
	tlp.update_time AS updateTime,
	tlp.update_by AS updateBy,
	date(tlp.create_time) as createDate
FROM t_lesson_plan tlp  
LEFT JOIN t_class tc ON tc.id = tlp.class_id 
LEFT JOIN t_course tce ON tce.id = tlp.course_id 
LEFT JOIN t_section ts ON ts.id = tlp.section_id 
LEFT JOIN t_customer_user tcu ON tcu.id = tlp.guide_teacher_id 
LEFT JOIN t_lesson_step tls ON tlp.id=tls.lesson_plan_id
LEFT JOIN t_school tss on tss.id=tcu.school_id
LEFT JOIN t_region r on r.id=tss.region_id
where tlp.valid_flag='0'
 ) a