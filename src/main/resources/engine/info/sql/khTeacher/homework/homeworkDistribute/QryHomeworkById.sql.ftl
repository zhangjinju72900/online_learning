SELECT
	h.id,
	h.`name`,
	h.class_id as classId,
	h.course_id as courseId,
	h.section_id as sectionId,
	h.homework_type as homeworkType,
	h.distribute_time as distributeTime,
	concat(h.end_time,'') as endTime,
	h.distribute_status as distributeStatus,
	h.difficulty_level as difficultyLevel,
	h.score,
	h.avg_score as avgScore,
	c.`name` as className,
	co.`name` as courseName,
	s.`name` as sectionName
FROM
	t_homework h
LEFT JOIN t_class c ON h.class_id = c.id
LEFT JOIN t_course co ON h.course_id = co.id
LEFT JOIN t_section s ON h.section_id = s.id where h.id = #{data.id}