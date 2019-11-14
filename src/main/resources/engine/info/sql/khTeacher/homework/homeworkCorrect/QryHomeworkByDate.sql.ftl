select * from (
	select
  	h.id,
  	h.homework_type as homeworkType,
	h.class_id as classId,
	h.name,
	d1.name as homeworkTypeName,
	c.name as className,
	h.create_by as createBy,
	case when h.end_time<now() then 1 else 0 end correctStatus
	from t_homework h
	left join t_dict d1
	on h.homework_type = d1.code
	and d1.cata_code = 't_homework.homework_type'
	left join t_class c
	on h.class_id = c.id
	 where DATE(distribute_time) = DATE(#{data.homeworkDate}) and h.create_by=#{data.teacherId} and  h.distribute_status=#{data.status}
 ) a
 