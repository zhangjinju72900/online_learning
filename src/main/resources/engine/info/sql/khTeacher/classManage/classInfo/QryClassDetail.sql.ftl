SELECT 
	tc.id AS id,
	tc.id AS classId,
	CONCAT(tc.grade,"级",tc.name) AS className,
	tc.grade AS grade,
	tc.class_type AS classType,
	td.name AS classTypeName,
(SELECT 
count(1)
FROM t_customer_user_class tcuc 
	join t_customer_user_role r on tcuc.customer_user_id = r.customer_user_id
LEFT JOIN t_customer_user tcu ON tcu.id = tcuc.customer_user_id 
LEFT JOIN t_dict td ON td.code = tcu.sex AND td.cata_code = 't_customer_user.sex' 
left join t_file_index i on tcu.file_id = i.id
where r.role_id = 10 and tcuc.class_id = tc.id) as studentCount
FROM t_class tc 
LEFT JOIN t_dict td ON td.code = tc.class_type AND td.cata_code = 't_class.class_type' 
where tc.id = #{data.classId}
