select * from (
	select  DISTINCT
	co.id as value,
	co.name as text
	from t_curriculum_course cuco
	left join t_curriculum cr
	on cuco.curriculum_id = cr.id
	left join t_course co
	on co.id = cuco.course_id
	left join t_class clcu
	on clcu.curriculum_id = cr.id
	where clcu.id=#{data.eq_classId} and co.visible_flag = 0 and co.valid_flag = 0 and cr.valid_flag = 0
 ) a 