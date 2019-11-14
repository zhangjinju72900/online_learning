SELECT * FROM(
select 
	p.id as id,
	p.information_id as informationId,
	p.pic_id as picId,
	p.create_time as createTime,
	p.create_by as createBy,
	p.update_time as updateTime,
	p.update_by as updateBy,
	cu.name as createName,
	cu1.name as updateName,
	f.filename as picName
	from t_information_pic p
	left join t_customer_user cu on p.create_by = cu.id
	left join t_customer_user cu1 on p.update_by = cu1.id
	left join t_information i
	on i.id=p.information_id
	left join t_file_index f on p.pic_id = f.id
	where p.information_id=#{data.id}
)a