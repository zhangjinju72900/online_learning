select f.file_type as fileType,SUBSTRING_INDEX(f.oss_key,'.',1) filekey,
case when (q.oss_key='' || q.oss_key=null) then f.oss_key else q.oss_key end ossKey, 
case when (q.bucket_name='' || q.bucket_name=null) then f.oss_url else q.bucket_name end bucket_name,
concat(f.filename,".",f.file_type) as fileName from t_question_file q left join t_file_index f on q.file_id = f.id  where q.question_id = #{data.id}