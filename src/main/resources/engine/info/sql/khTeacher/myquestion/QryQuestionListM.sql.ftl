 
  select * from (
	select
	q.id as id,
	q.id as questionId,
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
	d2.name as difficultyLevelName,
	d3.name as enableStatusName,
    d4.name as dataFlagType
	from t_question q

LEFT JOIN
	(
select b.*
from (
select DISTINCTROW
concat(tp.parent_id,tp.id),
	tp.id as id,
	CASE
		WHEN tp.parent_id = 0 THEN
			''
		ELSE
			tp.parent_id
		END AS pid,
	tp.name as text,
	'closed' as state
from t_professional tp
left join t_course tc on tp.id = tc.professional_id
left join t_curriculum_course tcc on tcc.course_id = tc.id
left join t_class tcl on tcc.curriculum_id = tcl.curriculum_id
left join t_customer_user_class tcuc on tcl.id = tcuc.class_id
left join t_customer_user tcu on tcuc.customer_user_id = tcu.id
where tcu.id = #{data.session.userInfo.userId} and tc.valid_flag = 0 and tp.valid_flag = 0

) a
LEFT JOIN (
	select 
	concat(c.professional_id,c.id),
	c.id as id,
	c.professional_id as pid,
	c.name  ,
  'closed' as state
	from  t_course c
	LEFT JOIN t_professional p ON c.professional_id=p.id 
	LEFT JOIN  t_customer_user tcu1 ON p.update_by=tcu1.id 
	LEFT JOIN  t_customer_user tcu2 ON p.create_by=tcu2.id 
	LEFT JOIN t_file_index f ON f.id=c.file_id
	where c.valid_flag=0
)b on b.pid=a.id

UNION all 

select DISTINCTROW
concat(tp.parent_id,tp.id),
	tp.id as id,
	0 AS pid,
	tp.name ,
	'closed' as state
from t_professional tp
left join t_course tc on tp.id = tc.professional_id
left join t_curriculum_course tcc on tcc.course_id = tc.id
left join t_class tcl on tcc.curriculum_id = tcl.curriculum_id
left join t_customer_user_class tcuc on tcl.id = tcuc.class_id
left join t_customer_user tcu on tcuc.customer_user_id = tcu.id
where tcu.id = #{data.session.userInfo.userId} and tc.valid_flag = 0 and tp.valid_flag = 0

)c
	on q.question_classify_id = c.id
	left join t_dict d1
	on q.question_type = d1.code 
	and d1.cata_code = 't_question_question_type'
	left join t_dict d2
	on q.difficulty_level = d2.code 
	and d2.cata_code = 't_homework_difficulty_level'
	left join t_dict d3 
	on q.enable_status = d3.code
	and d3.cata_code = 't_question_enable_status'
    left join t_dict d4
    on q.data_flag = d4.code
    and d4.cata_code = 't_question.data_flag'
	where q.valid_flag = 0 and q.data_flag = 1 and q.question_type != 3
	and q.teacher_id =#{data.session.userInfo.userId} order by q.create_time desc
 ) d
 
  