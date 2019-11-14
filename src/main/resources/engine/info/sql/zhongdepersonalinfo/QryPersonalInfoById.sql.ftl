select * from (
SELECT
	tcu.id as id,
	tcu.id as userId,
	tcu.nickname,
	tcu.name as name,
	tcu.card_num as cardNum,
	tcu.tel as tel,
	tcu.sex as sex,
	td.name as sexName,
	tcu.school_id as schoolId,
	tcu.file_id as fileId,
	ts.name as schoolName,
	tab1.classId,
	tab1.className,
	tcu.province_code as provinceCode,
  	tc.`name` AS provinceName,
	user_explain as userExplain
FROM  t_customer_user tcu
LEFT JOIN t_school ts on ts.id=tcu.school_id 
LEFT JOIN (select tcuc.customer_user_id, GROUP_CONCAT(tcuc.class_id) as classId, GROUP_CONCAT(tc.`name`) as className from t_customer_user_class tcuc 
LEFT JOIN t_class tc on tc.id = tcuc.class_id GROUP BY tcuc.customer_user_id)tab1 on tcu.id = tab1.customer_user_id
LEFT JOIN t_dict td on td.code = tcu.sex
AND td.cata_code='t_customer_user.sex'
LEFT JOIN t_city tc ON tcu.province_code = tc.`code`
 ) a 