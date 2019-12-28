select * from (
SELECT
q.id as id,
q.content as content,
q.question_classify_id as questionClassifyId,
q.question_type as questionType,
q.data_flag as dataFlag,
q.difficulty_level as difficultyLevel,
IF(q.valid_flag=0,'启用','禁用') as validFlag,
q.enable_status as enableStatus,
q.create_time as createTime,
c.name as questionClassifyName,
d1.name as questionTypeName,
d2.name as difficultyLevelName
from t_question q
left join t_dict d1
on q.question_type = d1.code
and d1.cata_code = 't_question_question_type'
left join t_dict d2
on q.difficulty_level = d2.code
and d2.cata_code = 't_homework_difficulty_level'
left join  t_professional c
on q.question_classify_id = c.id
where q.valid_flag = 0
order by q.create_time desc
) a