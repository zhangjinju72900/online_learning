select * from (
	select 
	c.id as id,
	c.id as courseId,
	c.name as name,
	c.professional_id as professionalId,
	c.course_count as courseCount,
	c.difficulty_level as difficultyLevel,
	c.remark as remark,
	c.file_id as fileId,
	f.filename as fileName,
	c.visible_flag as visibleFlag,
	case when c.visible_flag=0 then '是' else '否' end as visibleFlagName,
	c.valid_flag as validFlag,
	tcu1.name as updateByName,
	tcu2.name as createByName,
	c.update_by as updateBy,
	c.create_by as createBy,
	c.update_time as updateTime,
	c.create_time as createTime,
	c.show_order as showOrder
	from  t_course c
	LEFT JOIN t_professional p ON c.professional_id=p.id 
	LEFT JOIN  t_customer_user tcu1 ON p.update_by=tcu1.id 
	LEFT JOIN  t_customer_user tcu2 ON p.create_by=tcu2.id 
	LEFT JOIN t_file_index f ON f.id=c.file_id
	where c.valid_flag=0
	order by c.create_time desc
 ) a 
