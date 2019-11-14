select * from (
	SELECT 
	a.id as id,
	a.study_by as customeruserid,
	a.course_id as courseid,
	a.section_id as sectionid,
	a.label_id as labelid,
	a.customer_resources_id as resourceid,
	a.create_time as createtime,
	a.create_by as createby,
	a.update_time as updatetime,
	a.update_by as updateby
	from t_study_record a
	where study_by = #{data.session.userInfo.userId}
	and course_id = #{data.courseId}
	order by id desc limit 1
) a 
