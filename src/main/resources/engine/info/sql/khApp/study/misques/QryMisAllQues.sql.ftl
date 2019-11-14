SELECT *
FROM (
SELECT
DISTINCT
 s.id AS id,
 s.homework_id AS homeworkId,
 s.homework_detail_id AS homeworkDetailId,
 s.school_id AS schoolId,
 s.class_id AS classId,
 s.course_id AS courseId,
 s.section_id AS sectionId,
 s.question_id AS questionId,
 s.teacher_id AS teacherId,
 s.student_id AS studentId,
 s.valid_flag AS validFlag,
 s.create_time AS createTime,
 s.create_by AS createBy,
 s.update_time AS updateTime,
 s.update_by AS updateBy,
 d.status
FROM t_wrong_question_set s
LEFT JOIN t_homework_answer d on d.homework_id=s.homework_id and d.student_id=s.student_id
where s.student_id = #{data.userId} and s.valid_flag = 0  and d.`status`=3
) a