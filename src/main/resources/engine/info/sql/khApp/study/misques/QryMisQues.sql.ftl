SELECT *
FROM (
SELECT COUNT(t.id) AS number,
	 c.name AS courseName,
	 t.course_id as courseId,
	 t.student_id as studentId
	FROM t_wrong_question_set t
	LEFT JOIN t_course c ON t.course_id = c.id
	LEFT JOIN t_homework_answer d on d.homework_id=t.homework_id and d.student_id=t.student_id
	where t.student_id = #{data.userId} and t.valid_flag = 0 and d.`status`=3
	GROUP BY t.course_id
) a