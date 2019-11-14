insert into t_question
(content,question_classify_id,question_type,difficulty_level,enable_status,create_time,create_by,valid_flag,data_flag,teacher_id,update_by,answer_thought)
values
(#{data.content},#{data.questionClassifyId},#{data.questionType},#{data.difficultyLevel},#{data.enableStatus},now(),#{data.createBy},'0','0','0','0',#{data.answerThought})
  