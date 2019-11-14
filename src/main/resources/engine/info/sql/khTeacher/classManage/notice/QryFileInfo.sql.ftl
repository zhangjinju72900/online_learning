SELECT oss_key AS ossKey,
CASE
WHEN access_type = 'private' THEN oss_url
ELSE ''
END   AS bucketName
FROM t_file_index
WHERE id = #{data.fileId}
;