select * from (
	SELECT 
	label.id as id,
	label.name as name,
	label.label_type as labelType,
	tcl.course_id as courseId,
	tsl.section_id as sectionId,
	tcsr.customer_resources_id as customerResourcesId,
	case tcsr.section_id
	when '0' then '课程资源'
	else '小节资源' end as type,
	tcur.customer_user_id as userId
	from t_label label
	left join t_section_label tsl on tsl.label_id=label.id
	left join t_course_label  tcl on tcl.label_id=label.id
	left join t_course_section_resources tcsr on tcsr.label_id=label.id
	inner join t_label_role   tlr on tlr.label_id=label.id
	inner join t_customer_user_role tcur on tcur.role_id=tlr.role_id
	where t_label.valid_flag = '0'
) a 
