select *
from (
select id as id,
course_id as courseId,
section_id as sectionId,
label_id as labelId,
customer_resources_id as customerResourcesId,
course_resources_name as courseResourcesName,
oss_url as ossUrl,
content as content,
status as status,
school_id as schoolId,
teacher_id as teacherId,
valid_flag as validFlag
from t_course_resources_error_recovery tcrer
) a