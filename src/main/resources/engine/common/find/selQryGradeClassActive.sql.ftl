select * from(
 SELECT
  c.id as id,
  c.name as className,
  c.grade as gradeName
 FROM t_class c where valid_flag = 0 and c.school_id =#{data.id}
) tab 