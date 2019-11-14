
SELECT
	tab1.customer_user_id as id,
	u.`name`,
	u.file_id as fileId, IFNULL(i.oss_url,'') as fileUrl
FROM
	(
		SELECT
			cuc.customer_user_id
		FROM
			t_customer_user_class cuc 
			JOIN t_attend_class_record cr ON cuc.class_id = cr.class_id
			JOIN t_customer_user_role ur ON cuc.customer_user_id = ur.customer_user_id
			WHERE
				cr.id = #{data.id}
			AND ur.role_id = 10
		AND cuc.customer_user_id NOT IN (
			SELECT
				uc.student_id
			FROM
				t_attend_class_sign_record uc where uc.attend_class_record_id = #{data.id}
				and  uc.valid_flag = 0
			
		)
	) tab1
LEFT JOIN t_customer_user u ON tab1.customer_user_id = u.id
LEFT JOIN t_file_index i on u.file_id = i.id


