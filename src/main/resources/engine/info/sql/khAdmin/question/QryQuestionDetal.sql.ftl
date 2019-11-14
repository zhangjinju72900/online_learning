 SELECT *
FROM (
SELECT
 t.id AS id,
 tc.id AS tid,
 t.content AS content,
 t.question_classify_id AS questionClassifyId,
 d.name AS questionType,
 d1.name AS difficultyLevel,
 d2.name AS enableStatus,
 t.answer_thought AS answerThought,
 t.valid_flag AS validFlag,
 t.data_flag AS dataFlag,
 t.teacher_id AS teacherId,
 t.create_time AS createTime,
 t.create_by AS createBy,
 t.update_time AS updateTime,
 t.update_by AS updateBy,
 t.question_classify_id AS OrgId
FROM t_question t
LEFT JOIN t_question_classify tc ON t.question_classify_id = tc.id
LEFT JOIN t_dict d ON d.cata_code='t_question_question_type' AND d.code=t.question_type
LEFT JOIN t_dict d1 ON d1.cata_code='t_homework_difficulty_level' AND d1.code=t.difficulty_level
LEFT JOIN t_dict d2 ON d2.cata_code='t_question_enable_status' AND d2.code=t.enable_status
where t.valid_flag = 0   and  t.data_flag ='1'  order by t.create_time desc
) a
  