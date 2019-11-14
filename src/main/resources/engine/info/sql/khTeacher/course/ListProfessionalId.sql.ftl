select *
from (
select distinctrow tp.id as value,tp.name as text
from t_professional tp
left join t_course tc on tp.id = tc.professional_id
left join t_curriculum_course tcc on tcc.course_id = tc.id
left join t_class tcl on tcc.curriculum_id = tcl.curriculum_id
left join t_customer_user_class tcuc on tcl.id = tcuc.class_id
left join t_customer_user tcu on tcuc.customer_user_id = tcu.id
where tcu.id = #{data.session.userInfo.userId} and tc.valid_flag = 0 and tp.valid_flag = 0
) a