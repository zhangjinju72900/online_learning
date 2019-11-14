INSERT INTO t_course_resources_error_recovery(course_id,
		section_id,
		label_id,
		customer_resources_id,
		course_resources_name,
		file_id,
		oss_key,
		oss_url,
		content,
		status,
		school_id,
		teacher_id,
		valid_flag,
		create_time,
		create_by,
		update_time,
		update_by)

			select 
		#{data.courseId},
		#{data.sectionId},
		#{data.labelId},
		#{data.customerResourcesId},
		#{data.courseResourcesName},
		id, oss_key, oss_url, 
		#{data.content},
		0,
		(select school_id from t_customer_user where id =#{data.teacherId}),
		#{data.teacherId},
		0,
		now(),
		#{data.session.userInfo.empId},
		now(),
		#{data.session.userInfo.empId}
		from t_file_index where id =#{data.fileId}