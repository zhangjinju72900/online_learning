select * from (
SELECT
	tu.id as id,
	tu.name as name,
	tu.card_num as cardNum,
	tu.sex as sex,
	case when tu.sex = 0 then '男' else '女' end as sexName,
	tu.password as password,
	tu.tel as tel,
	tu.login_count as loginCount,
	tu.learn_time as learnTime,
	tu.last_login_time as lastLoginTime,
	tu.user_explain as userExplain,
	tab1.classId,
	tu.school_id as schoolId,
    tab1.className,
    ts.name as school,
    tab1.grade as grade,
    tab2.roleId,
    tab2.roleName,
    r.name as regionName,
    tu.create_time as createTime,
    tu.create_by as createBy,
    tu.update_time as updateTime,
    tu.update_by as updateBy
    
FROM t_customer_user  tu 

left join (select tcuc.customer_user_id, group_concat(tcuc.class_id) as classId, group_concat(tc.name)as className, group_concat(tc.grade) as grade from t_customer_user_class tcuc 
left join t_class tc on tc.id=tcuc.class_id where tc.valid_flag = 0 group by tcuc.customer_user_id) tab1 on tu.id = tab1.customer_user_id
left join t_school ts on ts.id=tu.school_id
LEFT JOIN t_region r on r.id=ts.region_id
join (select tcur.customer_user_id, group_concat(tcur.role_id) as roleId, group_concat(tr.name)as roleName from t_customer_user_role tcur 
left join t_role tr on tr.id=tcur.role_id where tcur.role_id = 11 group by tcur.customer_user_id)tab2 on tu.id = tab2.customer_user_id

 ) a 