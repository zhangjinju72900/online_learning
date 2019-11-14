select * from (
SELECT
	tf.id as id,
	tf.feedback as feedback,
	tf.feedback_by as feedbackBy,
	tf.feedback_time as feedbackTime,
	tf.create_time as createTime,
	tf.create_by as createBy,
	tf.update_time as updateTime,
	tf.update_by as updateBy
FROM t_feedback tf where tf.valid_flag = '0'
ORDER BY tf.create_time desc
 ) a 