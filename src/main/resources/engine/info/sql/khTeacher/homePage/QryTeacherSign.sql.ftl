SELECT * FROM(
		SELECT
			cu.id,
			cu.`name`,
			cu.integral,
			cu.nickname,
			IFNULL(sir.id,0) AS signType,
			s.`name` as schoolName
		FROM
			t_customer_user cu 
		left join t_school s on cu.school_id = s.id	
		LEFT JOIN (select 1 as id, user_id from t_sign_in_record where user_id = #{data.session.userInfo.userId} and to_days(sign_time) = TO_DAYS(CURRENT_DATE())) sir on cu.id = sir.user_id
		WHERE	cu.id = #{data.session.userInfo.userId}

)a


