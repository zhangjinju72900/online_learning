SELECT
	*
FROM
	(
		SELECT
			tcu.id AS id,
			tcu.id AS userId,
			tcu.nickname,
			tcu.NAME AS name,
			tcu.card_num AS cardNum,
			tcu.tel AS tel,
			tcu.sex AS sex,
			td. NAME AS sexName,
			tcu.school_id AS schoolId,
			ts. NAME AS schoolName,
			tab1.classId,
			tab1.className,
			dictgrade. NAME AS gradeName,
			tab1.teacherClassName,
			tcu.province_code AS provinceName,
			tcu.user_explain,
			tcu.file_id AS uFileId
		FROM
			t_customer_user tcu
		LEFT JOIN t_school ts ON ts.id = tcu.school_id
		LEFT JOIN (
			SELECT
				tcuc.customer_user_id,
				GROUP_CONCAT(
					CONCAT(tc.grade, '级', tc.`name`)
				) AS teacherClassName,
				GROUP_CONCAT(tcuc.class_id) AS classId,
				GROUP_CONCAT(tc.`name`) AS className
			FROM
				t_customer_user_class tcuc
			LEFT JOIN t_class tc ON tc.id = tcuc.class_id
			GROUP BY
				tcuc.customer_user_id
		) tab1 ON tcu.id = tab1.customer_user_id
		LEFT JOIN t_dict dictgrade ON tcu.grade = dictgrade. CODE
		AND dictgrade.cata_code = 't_customer_user.grade'
		LEFT JOIN t_dict td ON td. CODE = tcu.sex
		AND td.cata_code = 't_customer_user.sex'
		WHERE
			tcu.id = #{data.session.userInfo.userId}
	) a