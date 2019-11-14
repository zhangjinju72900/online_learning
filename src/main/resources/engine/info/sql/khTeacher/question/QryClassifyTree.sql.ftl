select * from (
select b.*
from (
select DISTINCTROW
concat(tp.parent_id,tp.id),
	concat(tp.id,'') as id,
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
	concat(c.id,'')  as id,
	c.professional_id as pid,
	c.name as text ,
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
	concat(tp.id,'')  as id,
	'0' AS pid,
	tp.name as text,
	'closed' as state
from t_professional tp
left join t_course tc on tp.id = tc.professional_id
left join t_curriculum_course tcc on tcc.course_id = tc.id
left join t_class tcl on tcc.curriculum_id = tcl.curriculum_id
left join t_customer_user_class tcuc on tcl.id = tcuc.class_id
left join t_customer_user tcu on tcuc.customer_user_id = tcu.id
where tcu.id = #{data.session.userInfo.userId} and tc.valid_flag = 0 and tp.valid_flag = 0

UNION ALL 

select
	'-1234' ,
	'0' as id,
	'' as pid,
	'中德诺浩课程' as text,
	'closed' as state
from dual
)c