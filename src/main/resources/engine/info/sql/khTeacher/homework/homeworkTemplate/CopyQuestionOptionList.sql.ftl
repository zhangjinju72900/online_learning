insert into t_question_answer_options(
question_id,
title,
content,
correct_flag,
create_time,
create_by,
update_time,
update_by
)

select 
#{data.qId},
title,
content,
correct_flag,
create_time,
create_by,
update_time,
update_by
from t_question_answer_options o  where o.question_id=#{data.questionId}