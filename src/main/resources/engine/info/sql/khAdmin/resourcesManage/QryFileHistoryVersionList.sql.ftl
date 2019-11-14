select * from (
SELECT
	id,
	name,
	file_type as fileType,
	file_path as filePath,
	parent_id as orgId,
	parent_id as parentId,
	backup_id as backupId,
	backup_type as backupType,
	version_code as versionCode,
	resources_type as resourcesType,
	create_by as createBy,
	create_time as createTime
	
FROM t_customer_resources e where backup_type = '0' and file_type != '' and valid_flag = '0'

 ) a 