SELECT
	filename AS fileName,
	file_type AS type,
	path AS path,
	oss_key as ossKey, 
	oss_url as ossUrl 
FROM t_file_index 
WHERE id = #{data.fileId} ; 