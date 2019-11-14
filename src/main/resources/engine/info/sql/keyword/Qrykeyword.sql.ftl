select * from (
	select 
	k.id as id,
	k.word as word,
	k.create_time as createTime,
	k.create_by,
	k.update_time as updateTime,
	k.update_by,
    c.name as creater,
    c2.name as updater
	from t_keyword k
    left join t_customer_user c on k.create_by=c.id
    left join t_customer_user c2  on k.update_by=c2.id
    where k.valid_flag = '0' 
 ) a 