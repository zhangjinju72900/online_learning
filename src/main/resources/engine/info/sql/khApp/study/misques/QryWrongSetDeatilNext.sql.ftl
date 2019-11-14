
	
	select 
	tab1.question_id as questionId, 
	tab1.course_id as courseId,
	tab1.student_id as studentId,
	q.content,
	q.id as questionId,
	q.question_type as questionType,
	q.difficulty_level as difficultyLevel,
	q.answer_thought as answerThought
from (
	SELECT
		DISTINCT question_id,
		t.course_id, 
		t.student_id 
	FROM
		t_wrong_question_set t
LEFT JOIN t_homework_answer d on d.homework_id=t.homework_id and d.student_id=t.student_id
	WHERE
	case when #{data.courseId} = ''
		then t.student_id =#{data.userId} and t.valid_flag = 0 
		else  t.student_id = #{data.userId} and t.valid_flag = 0 and t.course_id =#{data.courseId}
	end	  

	and d.`status`=3
		ORDER BY question_id asc
		
	)tab1 
	LEFT JOIN t_question q on tab1.question_id = q.id 
	