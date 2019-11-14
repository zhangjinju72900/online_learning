 SELECT *
FROM (
SELECT
 t.id AS id,
 t.content AS content,
 t.question_classify_id AS questionClassifyId,
 b.text AS questionClassifyName,
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
LEFT JOIN t_dict d ON d.cata_code='t_question_question_type' AND d.code=t.question_type
LEFT JOIN t_dict d1 ON d1.cata_code='t_homework_difficulty_level' AND d1.code=t.difficulty_level
LEFT JOIN t_dict d2 ON d2.cata_code='t_question_enable_status' AND d2.code=t.enable_status
LEFT JOIN (
	select 
	id as id,
	name as text
	from t_professional
	where valid_flag=0

union ALL
select 	
	c.id as id,
	c.name as text 
	from  t_course c
	LEFT JOIN t_professional p ON c.professional_id=p.id 
	where c.valid_flag=0
)b on b.id=t.question_classify_id
where t.valid_flag = 0 and t.data_flag=0 order by t.create_time desc
) a
  