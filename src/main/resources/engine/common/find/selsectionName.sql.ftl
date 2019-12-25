select * from(
SELECT
c.id,
c.name
FROM t_section c
WHERE c.course_id = #{data.id}
) tab