
	select id, DATE_FORMAT(start_time, '%H:%i') as attendClassDate, '已确认上课' as title
	 from t_attend_class_record where teacher_id = #{data.userId} and valid_flag = 0
		and #{data.roleId} = '11' and DATE_FORMAT(start_time, '%Y-%m-%d') = #{data.startTime}
union all 	
	SELECT
			s.id,
			CASE
		WHEN sr.signTime IS NULL THEN
			DATE_FORMAT(start_time, '%H:%i')
		ELSE
			DATE_FORMAT(sr.signTime, '%H:%i')
		END AS attendClassDate,
		 CASE
		WHEN sr.signTime IS NULL THEN
			'上课未签到'
		ELSE
			'上课签到'
		END AS title
		FROM
			t_attend_class_record s
		JOIN t_customer_user_class r ON s.class_id = r.class_id
		LEFT JOIN (
			SELECT
				attend_class_record_id,
				min(sign_time) AS signTime
			FROM
				t_attend_class_sign_record
			WHERE
				student_id = #{data.userId}
			AND valid_flag = 0
			AND DATE_FORMAT(sign_time, '%Y-%m-%d') = #{data.startTime}
			GROUP BY
				attend_class_record_id
		) sr ON s.id = sr.attend_class_record_id
		WHERE
			r.customer_user_id = #{data.userId}
		AND s.valid_flag = 0
		AND #{data.roleId} = '10'
		AND DATE_FORMAT(s.start_time, '%Y-%m-%d') = #{data.startTime}
	
