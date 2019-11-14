select * from (
	select 
	b.id as id,
	b.create_time as createTime,
	b.create_by as createby ,
	b.pic_id as fileId,
	b.update_time as updateTime,
	b.update_by as updateby ,
    f.filename as filename,
    b.show_order as showorder,
    b.alink as aLink,
    c.name as createrName,
    c2.name as updaterName,
    b.banner_type as type,
    t.`name` as typeName
	from t_banner b
	left join t_customer_user c on b.create_by=c.id
    left join t_customer_user c2  on b.update_by=c2.id
    left join t_file_index f  on f.id=b.pic_id
LEFT JOIN t_dict t on t.`code`=b.banner_type and t.cata_code='t_banner.banner_type'
    where b.valid_flag = '0' 
 ) a 