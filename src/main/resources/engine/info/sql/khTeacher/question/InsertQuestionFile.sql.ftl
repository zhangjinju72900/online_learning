insert into t_question_file(
	question_id,
	file_id,
	oss_key,
	bucket_name,
	create_time,
	create_by,
	update_time,
	update_by)
values (
	#{data.questionId},
	#{data.fileId},
	#{data.ossKey},
	#{data.bucketName},
	now(),
	#{data.session.userInfo.userId},
	now(),
	#{data.session.userInfo.userId})