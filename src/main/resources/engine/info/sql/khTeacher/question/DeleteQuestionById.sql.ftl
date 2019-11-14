update t_question set valid_flag = 1 where id = (select question_id from t_homework_detail where id = #{data.id}) and question_type = 3;
delete from t_homework_detail where id = #{data.id};