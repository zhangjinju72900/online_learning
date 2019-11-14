select *
from (
SELECT id  AS attendClassRecordId,
school_id  AS schoolId,
class_id   AS classId,
course_id  AS courseId,
section_id AS sectionId
FROM t_attend_class_record
order by id desc
) a where classId = #{data.classId}