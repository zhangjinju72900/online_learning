select * from (
	select 
	cu.id, 
	cu.name, 
	cu.card_num as cardNum, 
	cu.tel, 
	cu.sex, 
	case when cu.sex = 0 then '男' else '女' end as sexName,
	cu.province_code as provinceCode,
	cu.login_count as loginCount, 
	cu.learn_time as learnTime, 
	cu.last_login_time as lastLoginTime, 
	cu.email, 
	cu.school_id as schoolId,
	cu.file_id as fileId,
	cu.create_by as createBy,
	cu.create_time as createTime,
	cu.update_time as updateTime,
	cu.update_by as updateBy,
	cu.user_explain as userExplain,
	cu.nickname as nickName,
	ci.name as provinceName,
	cu.password as password,
    cu.password as password1,
    cu.password as testPassword,
	cu.password as updatePassword,
	s.name as school,
	c.grade as grade,
	dictgrade. NAME AS gradeName, 
	c.className, 
	r.roleId, 
	r.roleName,
	f.filename as fileName,
	cu1.name as createByName,
	cu2.name as updateByName
	from t_customer_user cu left join t_school s on cu.school_id = s.id
	left join (select cur.customer_user_id, GROUP_CONCAT(cur.role_id) as roleId, 
	GROUP_CONCAT(r1.name) as roleName from 
	t_customer_user_role cur left join t_role r1 on cur.role_id = r1.id 
	group by cur.customer_user_id) r on cu.id = r.customer_user_id
	left join (select cuc.customer_user_id, GROUP_CONCAT(c1.name) as className, 
	GROUP_CONCAT(c1.grade) as grade from 
	t_customer_user_class cuc left join t_class c1 on cuc.class_id = c1.id 
	group by cuc.customer_user_id) c on cu.id = c.customer_user_id
	left join t_file_index f on cu.file_id = f.id
	left join (select id,name from t_customer_user) cu1
	on cu1.id=cu.create_by
	left join (select id,name from t_customer_user) cu2
	on cu2.id=cu.update_by
	left join t_city ci
	on cu.province_code=ci.code
	LEFT JOIN t_dict dictgrade ON cu.grade = dictgrade. CODE
	AND dictgrade.cata_code = 't_customer_user.grade'
 ) a 