select *
from (
select s.id   as value,
s.NAME as text
from t_section s 
where s.course_id = #{data.courseId} and s.valid_flag = 0
) a