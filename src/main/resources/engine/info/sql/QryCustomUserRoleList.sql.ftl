SELECT
	*
FROM
	(
		SELECT
			ur.id,
			ur.customer_user_id AS userId,
			ur.role_id AS roleId,
			r. NAME,
			u. NAME AS userName,
			r.description,
			u1. NAME AS updateByName,
			u2. NAME AS createByName,
			r.update_by AS updateBy,
			u. NAME AS empName,
			r.create_by AS createBy,
			r.update_time AS updateTime,
			ur.create_time AS createTime,
			'本地服务器认证' AS authTypeName,
			d2. NAME AS statusName
		FROM
			t_customer_user_role ur
		LEFT JOIN t_role r ON ur.role_id = r.id
		LEFT JOIN t_customer_user u ON ur.customer_user_id = u.id
		LEFT JOIN t_customer_user u1 ON ur.customer_user_id = u1.id
		LEFT JOIN t_customer_user u2 ON ur.customer_user_id = u2.id
		LEFT OUTER JOIN t_dict d2 ON u.valid_flag = d2.CODE
		AND d2.cata_code = 't_information.valid_flag'
	) a 