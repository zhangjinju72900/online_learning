update t_wrong_question_set set valid_flag = 1
where question_id = #{data.questionId}
and student_id = #{data.userId}