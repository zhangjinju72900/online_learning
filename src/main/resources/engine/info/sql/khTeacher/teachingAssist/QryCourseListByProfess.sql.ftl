select * from(
	select tc.id as value,
	tc.name as text
	from t_course tc
	where tc.professional_id = #{data.professionalId}
	and tc.visible_flag = 0
) a