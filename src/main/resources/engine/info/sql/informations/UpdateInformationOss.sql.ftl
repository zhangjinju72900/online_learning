update t_information set oss_key=#{data.ossKey},
	bucket_name=#{data.bucketName}
	where id=#{data.informationId}
