select * from (

	select oss_key as ossKey,
	oss_url as bucketName,
	file_type as fileType,
	id
	from t_file_index
	where id = #{data.fileId} or oss_key=#{data.fileId}
 ) a 

