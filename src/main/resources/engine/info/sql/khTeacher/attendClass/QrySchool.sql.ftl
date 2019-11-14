select *
from (
SELECT c.school_id AS schoolId
FROM t_class c
WHERE c.id = #{data.classId}
) a