select id           as id,
question_id  as questionId,
title        as title,
content      as content,
correct_flag as correctFlag
from t_question_answer_options
where question_id = #{data.id}