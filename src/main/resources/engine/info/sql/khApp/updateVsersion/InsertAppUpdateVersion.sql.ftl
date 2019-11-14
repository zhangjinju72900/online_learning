INSERT INTO `t_app_update_file_version` (
	`base_id`,
	`base_type`,
	`valid_flag`,
	`create_time`,
	`create_by`,
	`update_time`,
	`update_by`
)
VALUES
	(
		#{data.baseId},
		#{data.baseType},
		'0',
		now(),
		#{data.session.userInfo.userId},
		now(),
		#{data.session.userInfo.userId}
	);

