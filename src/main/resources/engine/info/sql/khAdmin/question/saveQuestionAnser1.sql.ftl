update  t_question_answer_options
set question_id= (SELECT id  from t_question where content=#{data.content} and question_classify_id = #{data.questionClassifyId}),
title=#{data.title},
content=#{data.acontent},
correct_flag=#{data.correctFlag},
update_time=now(),
update_by=#{data.updateBy}



  