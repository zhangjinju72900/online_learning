SELECT * FROM(
	SELECT
		tn.id AS id,
		tn.title AS title,
		tn.context AS context,
		tn.source AS source,
		tnf.file_id AS fileId,
		u.`name` as releaseName,
		u.file_id as headPic,
		case when tnf.oss_key='' or tnf.oss_key is null then (select f.oss_key from t_file_index f where f.id=tnf.file_id) else tnf.oss_key end ossKey,
		case when tnf.bucket_name='' or tnf.bucket_name is null then (select f.oss_url from t_file_index f where f.id=tnf.file_id) else tnf.bucket_name end  bucketName,
		(select f.filename from t_file_index f where f.id=tnf.file_id) as fileName,
		tn.release_time AS releaseTime,
		tn.release_by AS releaseBy,
		tn.valid_flag AS validFlag,
		tn.school_id AS schoolId ,
		(select GROUP_CONCAT(cs.`name`) from t_notice_school s LEFT JOIN t_class cs on cs.id=s.class_id where s.notice_id=tn.id GROUP BY s.notice_id) as className
	from t_notice tn
	LEFT JOIN t_notice_file tnf ON tnf.notice_id = tn.id
LEFT JOIN t_customer_user u on u.id=tn.release_by ORDER BY tn.release_time desc
)a
