SELECT * FROM (
SELECT
tmm.id AS id,
tmm.parent_id AS parentId,
tmm.name AS name,
tmm.file_path AS filePath,
tmm.file_id AS fileId,
tmm.file_name AS fileName,
tmm.file_type AS fileType,
tmm.resources_type as resourcesType,
tfi.name as resourcesTypeName,
tmm.oss_key AS ossKey,
tmm.oss_url AS ossUrl,
tmm.valid_flag AS validFlag,
tmm.create_time AS createTime,
tmm.create_by AS createBy,
tmm.update_time AS updateTime,
tmm.update_by AS updateBy
FROM t_maintain_manual tmm
LEFT JOIN t_dict tfi ON tfi.code = tmm.resources_type
WHERE tmm.parent_id = #{data.id} and tmm.valid_flag=0 AND tmm.data_flag = 1 and tfi.cata_code = 't_ftp_upload_record.file_type'
) a
