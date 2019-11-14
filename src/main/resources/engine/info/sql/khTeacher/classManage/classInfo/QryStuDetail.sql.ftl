SELECT 
	tcu.id AS customerUserId,
	tcu.name AS stuName,
	tcu.tel AS tel,
	tcu.sex AS sex,
	d.name AS sexName,
	CONCAT(tcu.last_login_time,'') AS lastLoginTime,
	ifnull(i.oss_url,'') as ossUrl
FROM t_customer_user tcu left join t_dict d on tcu.sex = d.code 
left join t_file_index i on tcu.file_id = i.id
where tcu.id = #{data.customerUserId} and d.cata_code = 't_customer_user.sex'
