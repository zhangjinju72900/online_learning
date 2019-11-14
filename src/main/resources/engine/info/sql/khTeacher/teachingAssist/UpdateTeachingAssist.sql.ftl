update t_teaching_assist set
	name = #{data.name},
	professional_id = #{data.professionalId},
	course_id = #{data.courseId},
	section_id = #{data.sectionId},
	visible_flag = #{data.visibleFlag},
	file_id = #{data.fileId},
	oss_key = #{data.ossKey},
	bucket_name = #{data.bucketName},
	teacher_id = #{data.session.userInfo.userId},
	update_time = now(),
	update_by = #{data.session.userInfo.userId}
where id = #{data.id}