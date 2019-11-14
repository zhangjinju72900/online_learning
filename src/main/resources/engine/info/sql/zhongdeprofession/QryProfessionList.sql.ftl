select * from (
	select 
	p.id as id,
	p.name as name,
	p.parent_id as parent,
	p.remark as remark,
	p.data_flag as dataFlag,
	p.valid_flag as validFlag,
	tcu1.name as updateByName,
	tcu2.name as createByName,
	p.update_by as updateBy,
	p.create_by as createBy,
	p.update_time as updateTime,
	p.create_time as createTime 
	from  t_professional p
	LEFT JOIN t_customer_user tcu1 ON p.update_by=tcu1.id 
	LEFT JOIN t_customer_user tcu2 ON p.create_by=tcu2.id 
	where p.valid_flag = '0'
 ) a 