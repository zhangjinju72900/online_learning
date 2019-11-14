delete
from t_question_answer_options
where question_id = (select id from t_question where id = #{data.id} and teacher_id = #{data.session.userInfo.userId});
update t_question set valid_flag = 1 , update_time = now(), update_by = #{data.session.userInfo.userId} where  id = #{data.id} and teacher_id = #{data.session.userInfo.userId}