SELECT
	*
FROM
	(
		SELECT DISTINCT
			tapp.activity_id AS id,
			tapp.id as appId,
			tab1.title AS title,
			tab1.site AS site,
			tab1.start_time AS startTime,
			tab1.end_time as endTime,
			tab1.valid_flag AS validFlag,
			tapp.apply_by AS applyBy,
			tab1.file_id as picId,
			tab1.file_id as fileId,
			fi.oss_url as fileOssUrl,
			tab1.timeType
		FROM
			t_activity_apply tapp
		LEFT JOIN (
			SELECT
				a.id as activity_id,
				a.title,
				a.site,
				a.start_time,
				a.end_time,
				a.valid_flag,
				release_status,
				a.file_id,
				case
	              when a.start_time > now()
	                then '-1'
	              when now() >= a.start_time and a.end_time >= now()
	                then '0'
	              when now() >= a.end_time
	                then '1'
	       		end as timeType
			FROM
				t_activity a
		) tab1 ON tapp.activity_id = tab1.activity_id
		left join t_file_index fi on tab1.file_id = fi.id
		where tab1.valid_flag = 0 and tab1.release_status = 1 and tapp.apply_by = #{data.session.userInfo.userId}
	) a