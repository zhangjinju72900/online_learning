select * from(
	select
	p.id, 
	p.activity_id as activityId,
	p.pic_id as picId,
	f.filename as picName,
	p.create_by as createBy,
	p.create_time as createTime
	from t_activity_pic p
	left join t_file_index f on p.pic_id = f.id
	where p.activity_id = #{data.id}
)tab