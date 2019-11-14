INSERT INTO `t_study_record` (
	`study_by`,
	`start_time`,
	`course_id`,
	`section_id`,
	`label_id`,
	`customer_resources_id`,
	`create_time`,
	`create_by`,
	`update_time`,
	`update_by`
)
VALUES
	(
		#{data.userId},
		now(),
		#{data.courseId},
		#{data.sectionId},
		#{data.labelId},
		#{data.resourcesId},
		now(),
		#{data.userId},
		now(),
		#{data.userId}
	)

