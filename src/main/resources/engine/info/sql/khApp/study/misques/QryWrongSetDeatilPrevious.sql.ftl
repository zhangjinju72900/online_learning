select 
	tab1.question_id as questionId, 
	tab1.course_id as courseId,
	tab1.student_id as studentId,
	q.content,
	q.question_type as questionType,
	q.difficulty_level as difficultyLevel,
	q.answer_thought as answerThought,
	(
	SELECT
		count(1) as nextCount
	FROM
		t_wrong_question_set 
	WHERE
	case when #{data.courseId} = ''
		then student_id = #{data.userId} and valid_flag = 0 and question_id > #{data.questionId}
		else  student_id = #{data.userId} and valid_flag = 0 and question_id > #{data.questionId} and course_id = #{data.courseId}
	end
	)as nextFlag,
	(
	SELECT
		count(1) as nextCount
	FROM
		t_wrong_question_set 
	WHERE
	case when #{data.courseId} = ''
		then student_id = #{data.userId} and valid_flag = 0 and question_id < #{data.questionId}
		else  student_id = #{data.userId} and valid_flag = 0 and question_id < #{data.questionId} and course_id = #{data.courseId}
	end	
	)as previousFlag
from (
	SELECT
		DISTINCT question_id,
		course_id, 
		student_id
	FROM
		t_wrong_question_set 
	WHERE
	case when #{data.courseId} = ''
		then student_id = #{data.userId} and valid_flag = 0 and question_id < #{data.questionId}
		else  student_id = #{data.userId} and valid_flag = 0 and question_id < #{data.questionId} and course_id = #{data.courseId}
	end	  
		ORDER BY question_id desc
		limit 1
	)tab1 
	LEFT JOIN t_question q on tab1.question_id = q.id 
	where q.enable_status = 0 and q.valid_flag = 0