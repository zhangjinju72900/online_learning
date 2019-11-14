update t_question
set content = #{data.content},
question_classify_id = #{data.questionClassifyId},
question_type = #{data.questionType},
difficulty_level = #{data.difficultyLevel},
enable_status = #{data.enableStatus},
answer_thought = #{data.answerThought},
update_time = now(),
update_by = #{data.session.userInfo.userId}
where id = #{data.id}