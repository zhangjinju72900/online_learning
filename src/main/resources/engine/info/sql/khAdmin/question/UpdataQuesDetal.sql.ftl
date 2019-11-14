update t_question 
set 
content = #{data.content}, 
question_classify_id = #{data.questionClassifyId},
question_type = #{data.questionType},
difficulty_level = #{data.difficultyLevel},
enable_status = #{data.enableStatus},
update_by = #{data.session.userInfo.empId},
update_time = now(),
 answer_thought = #{data.answerThought}
where id = #{data.updateBy}


  