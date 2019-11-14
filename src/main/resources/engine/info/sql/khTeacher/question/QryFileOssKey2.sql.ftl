select * from (
	select name as ProfessionalName
	from t_professional
	where id = #{data.professionalId}
 ) a 

