select *
from (
select tcsr.customer_resources_id as customerResourcesId,
case when tcsr.course_resources_name = '' then r.name
	else tcsr.course_resources_name end as courseResourcesName,
tcsr.course_id             as courseId,
tcsr.section_id            as sectionId,
tcsr.label_id              as labelId,
r.file_id 				   as fileId
from t_course_section_resources tcsr
left join t_customer_resources r on tcsr.customer_resources_id = r.id 
where tcsr.id = #{data.courseSectionResourcesId}
) a