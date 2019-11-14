SELECT
	 tmm.id AS id,
	 tmm.parent_id AS parentId,
	 tmm.name AS name,
	 tmm.file_path AS filePath,
	 tmm.file_id AS fileId,
	 concat(tmm.file_name,'.',tmm.file_type) AS fileName,
	 tmm.file_type AS fileType,
	 tmm.resources_type as resourcesType,
	 tmm.oss_key AS ossKey,
	 tmm.oss_url AS ossUrl,
	 tmm.valid_flag AS validFlag,
	 tmm.create_time AS createTime,
	 tmm.create_by AS createBy,
	 tmm.update_time AS updateTime,
	 tmm.update_by AS updateBy 
FROM t_maintain_manual tmm 
WHERE tmm.id = #{data.id} and tmm.data_flag='1'
