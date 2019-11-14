select * from (
SELECT
	e.id,
	e.name,
	e.file_type as fileType,
	e.file_path as filePath,
	e.parent_id as orgId,
	e.parent_id as parentId,
	e.backup_type as backupType,
	e.version_code as versionCode,
	e.resources_type as resourcesType,
	tfi.name as resourcesTypeName,
	e.oss_key as ossKey,
	e.create_by as createBy,
	e.create_time as createTime
FROM t_customer_resources e LEFT JOIN t_dict tfi ON tfi.code = e.resources_type 
where e.backup_type = '1' and e.file_type != '' and e.valid_flag=0 and tfi.cata_code = 't_ftp_upload_record.file_type'

 ) a 