select *
from (
SELECT 
	t.id,
	t.name,
	t.professional_id  as professionalId,
	t.course_id as  courseId,
	t.section_id as  sectionId,
	t.file_id  as fileId,
	t.oss_key  as ossKey,
	t.bucket_name as bucketName,
	t.teacher_id as teacherId,
	t.create_time as createTime,
	t.create_by as createBy,
  	t.update_time  as updateTime,
  	t.update_by as updateBy,
  	cu.name as teacherName
	FROM t_teaching_assist  t
	left join t_customer_user cu
	on cu.id = t.teacher_id
	where t.valid_flag = 0 and t.visible_flag=0
) a