INSERT INTO t_course_label
(
	course_id,
	label_id,
	create_time,
	create_by,
	update_time,
	update_by
)
SELECT 
	#{data.courseId},
	label_id,
	NOW(),
	#{data.session.userInfo.userId},
	NOW(),
	#{data.session.userInfo.userId}
from t_course_label WHERE course_id=#{data.id}