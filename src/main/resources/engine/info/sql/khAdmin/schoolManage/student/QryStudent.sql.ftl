select * from(
SELECT
			tu.id AS id,
			tu.name,
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
			tc.grade AS gradeName,
			tcur.role_id AS roleId,
			tr. NAME AS roleName,
			date(tu.create_time) AS createTime,
			tu.create_by AS createBy,
			tu.update_time AS updateTime,
			tu.update_by AS updateBy,
			r.`name` as regionName
		FROM
			t_customer_user tu
		LEFT JOIN t_customer_user_class tcuc ON tu.id = tcuc.customer_user_id
		LEFT JOIN t_class tc ON tc.id = tcuc.class_id
		LEFT JOIN t_school ts ON ts.id = tu.school_id
		LEFT JOIN t_customer_user_role tcur ON tu.id = tcur.customer_user_id
		JOIN t_role tr ON tr.id = tcur.role_id
		LEFT JOIN t_dict dictsex ON tu.sex = dictsex. CODE
		AND dictsex.cata_code = 't_customer_user.sex'
		LEFT JOIN t_dict dictgrade ON tu.grade = dictgrade. CODE
		AND dictgrade.cata_code = 't_customer_user.grade'
		LEFT JOIN t_region r on r.id=ts.region_id
		WHERE
			tcur.role_id = 10 and (tc.valid_flag = 0 or tc.valid_flag is null)
		and tu.name LIKE CONCAT('%',#{data.name},'%')
		and case when ts.name is null then 1=1 else ts.name LIKE CONCAT('%',#{data.school},'%') end
		order by createTime desc
) a