select * from (
	SELECT
		t3.id,
		tcr.name,
		tcr.file_type as fileType,
		tcr.file_path as filePath,
		tcr.parent_id as orgId,
		tcr.parent_id as parentId,
		tcr.backup_type as backupType,
		tcr.version_code as versionCode,
		tcr.resources_type as resourcesType,
		tcr.oss_key as ossKey,
		tcr.create_by as createBy,
		tcr.create_time as createTime
	FROM
		(
			SELECT
				t1.id,
				t1.parent_id AS orgId,
			IF (
				find_in_set(parent_id, @pids) > 0,
				@pids := concat(@pids, ',', id),
				0
			) AS ischild
			FROM
				(
					SELECT
						id,
						parent_id
					FROM
						t_customer_resources t
					WHERE
						backup_type = '1' and valid_flag = '0'
					ORDER BY
						parent_id,
						id
				) t1,
				(SELECT @pids := #{data.id}) t2
		) t3 left join t_customer_resources tcr on t3.id = tcr.id
	WHERE
		t3.ischild != 0
	AND tcr.file_type != ''
 ) a 