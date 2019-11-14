select 
	customer_resources_id as resourceId,
	section_id	as sectionId,
	label_id as labelId
from 
	t_course_section_resources 
where course_id = #{data.id}