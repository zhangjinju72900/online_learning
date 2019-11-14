select * from(
SELECT
c.id as id,
c.school_id as schoolId,
c.name as className,
c.grade as gradeName
FROM t_class c
where c.school_id = #{data.id} and c.valid_flag=0
) tab