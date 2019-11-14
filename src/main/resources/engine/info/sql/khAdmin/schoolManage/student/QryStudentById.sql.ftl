select * from (
SELECT
			tu.id AS id,
			tu.name AS name,
			tu.card_num AS cardNum,
			tu.sex AS sex,
			tu. PASSWORD AS PASSWORD,
			tu.tel AS tel,
			tu.login_count AS loginCount,
			tu.learn_time AS learnTime,
			tu.last_login_time AS lastLoginTime,
			tu.user_explain AS userExplain,
			tu.grade AS grade,
			tcuc.class_id AS classId,
			tu.school_id AS schoolId,
			tc. NAME AS className,
			dictsex. NAME AS sexName,
			ts. NAME AS school,
			dictgrade. NAME AS gradeName,
			tcur.role_id AS roleId,
			tr. NAME AS roleName,
			tu.create_time AS createTime,
			tu.create_by AS createBy,
			tu.update_time AS updateTime,
			tu.update_by AS updateBy
		FROM
			t_customer_user tu
		LEFT JOIN t_customer_user_class tcuc ON tu.id = tcuc.customer_user_id
		LEFT JOIN t_class tc ON tc.id = tcuc.class_id
		LEFT JOIN t_school ts ON ts.id = tu.school_id
		LEFT JOIN t_customer_user_role tcur ON tu.id = tcur.customer_user_id
		JOIN t_role tr ON tr.id = tcur.role_id
		LEFT JOIN t_dict dictsex ON tu.sex = dictsex. CODE
		AND dictsex.cata_code = 't_customer_user.sex'
		LEFT JOIN t_dict dictgrade ON tc.grade = dictgrade. CODE
		AND dictgrade.cata_code = 't_customer_user.grade'
		WHERE
			tcur.role_id = 10
			)tab1 