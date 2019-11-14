select concat(pid, id),
id,
pid,
text,
type,
typeName,
remark,
fileType,
csrId
from (
select 0                       as csrId,
s.id as id,
''                      as pid,
'course'                as type,
s.`name`                AS text,
'课程'                    as typeName,
s.remark,
''                      as fileType,
0                       as show_order,
s.create_time
from t_course s
where id = #{data.courseId}
UNION ALL
SELECT 0                              as csrId,
s.id AS id,
s.course_id AS pid,
'course-section'               as type,
s.`name`                       AS text,
'课程-小节'                        as typeName,
s.remark                       as remark,
''                             as fileType,
s.show_order,
s.create_time
FROM t_section s
WHERE s.course_id = #{data.courseId} 
and valid_flag = '0'
) a
ORDER BY 
a.id