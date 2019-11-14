INSERT INTO `t_file_index` (
	`uuid`,
	`file_type`,
	`path`,
	`create_time`,
	filename,
	length,
	storage_type,
	access_type,
	create_by,
	oss_key
)
VALUES
	(
		#{data.uuid},
		#{data.filetype},
		#{data.path},
		now(),
		#{data.uuid},
		0,
		'public',
		'public',
		#{data.userId},
		#{data.key}
	);

