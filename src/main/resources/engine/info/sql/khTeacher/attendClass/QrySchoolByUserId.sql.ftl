select *
from (
SELECT c.school_id AS schoolId
FROM t_customer_user c
WHERE c.id = #{data.studentId}
) a