INSERT INTO `t_app_update_file_version` (
	`base_id`,
	`base_type`,
	`valid_flag`,
	`create_time`,
	`create_by`,
	`update_time`,
	`update_by`
)
select id, 0, 0, now(), #{data.session.userInfo.userId}, now(), #{data.session.userInfo.userId} 
	from t_banner where id = #{data.id} and banner_type = 2

