delete
from t_question_answer_options
where question_id = #{data.id};
update t_question set valid_flag = 1
where  id = #{data.id}