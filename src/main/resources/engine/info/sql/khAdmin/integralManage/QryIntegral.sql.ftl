select * from (
SELECT 
	tu.id as id,
	tu.name as name,
	tu.card_num as cardNum,
	tu.sex as sexNo,
	d.name as sex,
	tu.integral as Integral,
	tu.total_integral as totalIntegral,
	tab1.classId,
    tab1.className,
    ts.id as school_id,
    ts.name as schoolName,
    tu.total_integral-tu.integral as aluseIntegral,
    tab1.`name` as grade,
    10 Entegral,
    tu.school_id as schoolId
   

	
FROM t_customer_user  tu

left join (select t.name,tcuc.customer_user_id, group_concat(tcuc.class_id) as classId, group_concat(tc.name)as className from t_customer_user_class tcuc 
left join t_class tc on tc.id=tcuc.class_id 
LEFT JOIN t_dict t on t.`code`=tc.grade and t.cata_code = 't_customer_user.grade' group by tcuc.customer_user_id) tab1 on tu.id = tab1.customer_user_id
lEFT JOIN t_school ts on ts.id=tu.school_id
left join t_dict d on d.code=tu.sex and d.cata_code = 't_customer_user.sex'
 ) a
