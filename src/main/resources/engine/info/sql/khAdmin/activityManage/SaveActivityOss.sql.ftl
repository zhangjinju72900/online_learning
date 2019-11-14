update t_activity 
	set 
	oss_key=#{data.ossKey},
	bucket_name=#{data.bucketName}
	where id=#{data.activityId}