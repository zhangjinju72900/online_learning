update t_question_answer_options
set content   = #{data.content},
question_id = #{data.questionId},
title = #{data.title},
correct_flag = #{data.correctFlag},
update_time = now(),
update_by = #{data.session.userInfo.userId}
where id = #{data.id}