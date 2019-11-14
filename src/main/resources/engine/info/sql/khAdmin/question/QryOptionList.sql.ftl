select t.id,
			 t.content ,
			  t.correct_flag as correctFlag,
t.title 
from t_question_answer_options t
where question_id = #{data.questionId} order by id 