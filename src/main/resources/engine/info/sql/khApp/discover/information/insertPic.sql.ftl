insert into t_information_pic(
	information_id, 
	pic_id, 
	valid_flag, 
	create_time, 
	create_by, 
	update_time, 
	update_by
	)
    VALUES (
    #{data.informationId},
    #{data.picId},
    0,
    now(),
    #{data.session.userInfo.userId},
    now(),
    #{data.session.userInfo.userId}
    )