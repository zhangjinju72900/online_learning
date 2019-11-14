INSERT INTO t_activity_config(
		`activity_id`,
		`rtmp_url`,
		`flv_url`,
		`mu_url`,
		`valid_flag`,
		`create_time`,
		`create_by`,
		`update_time`,
		`update_by`
		)
		VALUES
		(
		#{data.activityId},
		#{data.rtmpUrl},
		#{data.flvUrl},
		#{data.muUrl},
		'0',
		now(),
		#{data.session.userInfo.userId},
		now(),
		#{data.session.userInfo.userId}
	);
		
