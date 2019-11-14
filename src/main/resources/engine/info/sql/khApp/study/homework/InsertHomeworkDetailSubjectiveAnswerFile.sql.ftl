INSERT INTO `t_homework_detail_subjective_answer_file` (
	`homework_detail_answer_id`,
	`file_id`,
	`oss_key`,
	`oss_url`,
	`create_time`,
	`create_by`,
	`update_time`,
	`update_by`
)
	SELECT
		#{data.homeworkDetailAnswerId},
		#{data.fileId},
		oss_key,
		oss_url,
		now(),
		#{data.userId},
		now(),
		#{data.userId}
from t_file_index
	WHERE
		id = #{data.fileId}