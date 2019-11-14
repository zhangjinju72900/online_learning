SELECT * FROM(
select 
	p.id as id,
	p.file_id as videoId,
	p.release_time as createTime,
	p.release_by as createBy,
	p.update_time as updateTime,
	p.update_by as updateBy,
	cu.name as createName,
	cu1.name as updateName,
	f.filename as videoName
	from t_information  p
	left join t_customer_user cu on p.release_by = cu.id
	left join t_customer_user cu1 on p.update_by = cu1.id
	left join t_file_index f on p.file_id = f.id
	where p.id=#{data.id}
)a