insert into t_question_answer_options
(question_id,title,content,correct_flag,create_time,create_by,update_by)
values
(#{data.questionId},
 #{data.title},#{data.acontent},#{data.correctFlag},now(),#{data.createBy},#{data.createBy})