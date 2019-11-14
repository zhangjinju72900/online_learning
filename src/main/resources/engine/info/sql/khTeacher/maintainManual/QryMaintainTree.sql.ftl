select * from (
	select
	m.id as id,
	CASE
	WHEN m.parent_id = 0 THEN ''
	ELSE m.parent_id
	END AS pid,
	x.file_type as fileType,
	m.oss_url as ossUrl,
	name as text,
	'closed' as state
	from t_maintain_manual m left join t_file_index x on m.file_id = x.id
	where m.valid_flag=0 and m.data_flag=0
) a