select * from (
	select 
	homework_template_id as homeworkTemplateId
	from t_homework_template_detail 
	where id = #{data.id}
 ) a 