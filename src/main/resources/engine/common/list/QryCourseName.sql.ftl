select * from (
	select 
	DISTINCT
	tc.id as value,
	tc.name as text
	from t_course tc
	where tc.professional_id=#{data.eq_professionalId}
 ) a 