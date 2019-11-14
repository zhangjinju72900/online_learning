select * from (
SELECT 
	tcu.id,
	tcuc.class_id as classId,
	tcuc.customer_user_id AS customerUserId,
	tcu.name AS stuName,
	tcu.file_id AS fileId,
	ifnull(i.oss_url,'') as ossUrl
FROM t_customer_user_class tcuc 
	join t_customer_user_role r on tcuc.customer_user_id = r.customer_user_id
LEFT JOIN t_customer_user tcu ON tcu.id = tcuc.customer_user_id 
LEFT JOIN t_dict td ON td.code = tcu.sex AND td.cata_code = 't_customer_user.sex' 
left join t_file_index i on tcu.file_id = i.id
where r.role_id = 10 and tcuc.class_id = #{data.classId}
)tab1