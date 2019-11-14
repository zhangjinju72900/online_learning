insert into t_question_answer_options (question_id,
title,
content,
correct_flag,
create_time,
create_by,
update_time,
update_by)
values (
#{data.questionId},
#{data.title},
#{data.content},
#{data.correctFlag},
now(),
#{data.session.userInfo.userId},
now(),
#{data.session.userInfo.userId}
)