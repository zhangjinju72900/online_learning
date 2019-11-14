	insert  into t_information_like(
	review_id,
	information_id,
	like_by,
	like_time,
	create_time,
	create_by,
	update_time,
	update_by)
	values(
	#{data.id},
	#{data.infoId},
	#{data.createBy},
	NOW(),
	NOW(),
	#{data.createBy},
	NOW(),
	#{data.createBy}
	);
	
	
	