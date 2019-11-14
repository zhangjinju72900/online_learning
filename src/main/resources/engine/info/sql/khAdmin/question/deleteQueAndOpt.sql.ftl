delete 
from t_question
where id = #{data.questionId}; 
delete from t_question_answer_options where question_id = #{data.questionId}
