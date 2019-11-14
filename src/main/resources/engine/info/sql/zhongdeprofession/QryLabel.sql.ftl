SELECT * FROM(
	SELECT
	id as value,
	name as text
	FROM t_label 
where case when #{data.courseorsectionId} like 'sec-%' then 
	label_type = 1 and id not in(
				select  label_id from t_section_label where section_id=#{data.scId} group by label_id	
	)
	else
	label_type = 0 and id not in(
				select  label_id from t_course_label where course_id=#{data.scId} group by label_id
	)end
and valid_flag=0
)a
