select * from (
	select 
		`id`,
		`activity_id` as activityId,
		`rtmp_url` as rtmpUrl,
		`flv_url` as flvUrl,
		`mu_url` as muUrl
	from t_activity_config where activity_id = #{data.activityId}	
)a