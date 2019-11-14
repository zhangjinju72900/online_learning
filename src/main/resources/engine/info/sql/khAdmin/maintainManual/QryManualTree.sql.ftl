SELECT * FROM (
SELECT
	 tmm.id AS id,
	 tmm.parent_id AS parentId,
	 tmm.name AS name,
	 tmm.file_path AS filePath,
	 tmm.file_id AS fileId,
	 tfi.filename AS fileName,
	 tmm.file_type AS fileType,
	 tmm.oss_key AS ossKey,
	 tmm.oss_url AS ossUrl,
	 tmm.valid_flag AS validFlag,
	 tmm.create_time AS createTime,
	 tmm.create_by AS createBy,
	 tmm.update_time AS updateTime,
	 tmm.update_by AS updateBy 
FROM t_maintain_manual tmm 
LEFT JOIN t_file_index tfi ON tfi.id = tmm.file_id 
) a
