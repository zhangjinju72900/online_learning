SELECT
	CONVERT(avg(s.score),DECIMAL(8,2)) AS resourceAvgScore
FROM
	t_course_resources_score s
JOIN t_customer_user_class u ON s.customer_user_id = u.customer_user_id
JOIN t_customer_user_role r ON u.customer_user_id = r.customer_user_id
WHERE
	s.customer_resources_id = #{data.resourceId}
and s.course_id = #{data.courseId}
AND u.class_id = #{data.classId}
AND r.role_id = 10;