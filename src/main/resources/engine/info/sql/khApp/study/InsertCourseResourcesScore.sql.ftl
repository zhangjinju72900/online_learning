	insert into t_course_resources_score(customer_resources_id,
	course_id,section_id,label_id,customer_user_id,score,pass_score,percent_score,pass_percent,
	create_time,create_by,update_time,update_by)
	values(
	#{data.customerResourcesId},
	#{data.courseId},
	#{data.sectionId},
	#{data.labelId},
	#{data.customerUserId},
	#{data.score},
	#{data.passScore},
	#{data.percentScore},
	#{data.passPercent},
	now(),
	#{data.customerUserId},
	now(),
	#{data.customerUserId}
	)