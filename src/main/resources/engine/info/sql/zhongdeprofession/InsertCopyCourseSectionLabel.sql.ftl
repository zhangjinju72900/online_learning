INSERT INTO t_section_label
(
	section_id,
	label_id,
	create_time,
	create_by,
	update_time,
	update_by
)

SELECT
	DISTINCT
		#{data.secId},
		sl.label_id,
		sl.create_time,
	#{data.session.userInfo.userId},
	sl.update_time,
	#{data.session.userInfo.userId} 
	FROM (SELECT * FROM t_section_label WHERE section_id IN(
			SELECT id FROM t_section WHERE course_id=#{data.courseId} and  section_id=#{data.section_id} and label_id=#{data.label_id} and valid_flag = '0'
		)) sl

