SELECT *
FROM (
SELECT 
o.id as id,
question_id as questionId,
d.name         AS correctFlagName,
if(o.correct_flag=0,'是','否') as correctFlag,
content AS content,
title AS title
FROM t_question_answer_options o left join t_dict d on o.correct_flag = d.code
WHERE question_id = #{data.questionId} and d.cata_code = 't_course.visible_flag' order by o.id
) a
  