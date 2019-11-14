insert into t_question_answer_options
(question_id,title,content,correct_flag,create_time,create_by,update_by)
values
((SELECT id  from t_question where content=#{data.content} and question_classify_id = #{data.questionClassifyId} 
		and question_type = #{data.questionType} and valid_flag=0),
 #{data.title},#{data.acontent},#{data.correctFlag},now(),#{data.createBy},'0')
  