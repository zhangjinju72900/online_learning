INSERT INTO `t_class_sign_rate` (
	`school_id`,
	`class_id`,
	`data_date`,
	`sign_in_count`,
	`real_sign_in_count`,
	`sign_in_rate`,
	`create_time`,
	`create_by`,
	`update_time`,
	`update_by`
)
VALUES
	(
		#{data.schoolId},
		#{data.classId},
		#{data.dataDate},
		#{data.studentCount},
		#{data.realStudentCount},
		#{data.rate},
		now(),
		'0',
		now(),
		'0'
	);

