select * from (
	select 
	t.id as id,
	t.name as name,
	t.create_time as createTime,
	t.create_by,
	t.update_time as updateTime,
	t.update_by,
    c.name as creater,
    c2.name as updater
	from t_region t
	left join t_customer_user c on t.create_by=c.id
    left join t_customer_user c2  on t.update_by=c2.id
	WHERE t.valid_flag = 0
 ) a 