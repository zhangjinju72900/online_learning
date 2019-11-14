select * from (
	select 
	tlp.student_count as studentCount,
	tcu.name as teachername,
	tlp.class_id AS classId,
	CONCAT(tc.grade,"级",tc.name) AS text
	from t_lesson_plan tlp 
	LEFT JOIN t_customer_user_class tcuc ON tlp.class_id = tcuc.class_id
	LEFT JOIN t_class tc ON tc.id = tcuc.class_id 
	LEFT JOIN t_customer_user as tcu ON tcu.id = tlp.guide_teacher_id
	WHERE tcuc.customer_user_id = #{data.session.userInfo.userId}
 ) a 
 