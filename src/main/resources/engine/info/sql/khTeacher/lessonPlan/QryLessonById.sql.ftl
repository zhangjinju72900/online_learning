select * from (
SELECT 
	tlp.id AS id,
	tlp.name AS name,

	CONCAT(tc.grade,"级",tc.name) AS className,

	tce.name AS courseName,

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
	tlp.create_by AS createBy,
	tlp.update_time AS updateTime,
	tlp.update_by AS updateBy 
FROM t_lesson_plan tlp  
LEFT JOIN t_class tc ON tc.id = tlp.class_id 
LEFT JOIN t_course tce ON tce.id = tlp.course_id 
LEFT JOIN t_section ts ON ts.id = tlp.section_id 
LEFT JOIN t_customer_user tcu ON tcu.id = tlp.guide_teacher_id 
LEFT JOIN t_lesson_step tls ON tls.lesson_plan_id = tlp.id 
where tlp.valid_flag='0' and tlp.id=#{data.id} 
 ) a