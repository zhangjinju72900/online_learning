update t_activity_config set rtmp_url = #{data.rtmpUrl},
	flv_url = #{data.flvUrl},
	mu_url = #{data.muUrl} where id = #{data.id}