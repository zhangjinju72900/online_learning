select * from (
	select 
	cu.id, 
	cu.name, 
	cu.card_num as cardNum, 
	cu.tel,  
	case when cu.sex = 0 then '男' else '女' end as sexName,
	cu.login_count as loginCount, 
	cu.learn_time as learnTime, 
	cu.last_login_time as lastLoginTime, 
	cu.email,
	s.`name` as school,
	td.NAME as grade, 
	c.className, 
	r.roleId,
	cu.school_id schoolId,
	r.roleName
	from t_customer_user cu 
LEFT JOIN t_school s on s.id =cu.school_id
	left join (select cur.customer_user_id, GROUP_CONCAT(cur.role_id) as roleId, 

	GROUP_CONCAT(r1.name) as roleName from 
	t_customer_user_role cur left join t_role r1 on cur.role_id = r1.id 
	group by cur.customer_user_id) r on cu.id = r.customer_user_id
	left join (select cuc.customer_user_id, GROUP_CONCAT(c1.name) as className, 
	GROUP_CONCAT(c1.grade) as grade from 
	t_customer_user_class cuc left join t_class c1 on cuc.class_id = c1.id 
	group by cuc.customer_user_id) c on cu.id = c.customer_user_id
	left join t_dict td on cu.grade = td.CODE
	AND td.cata_code = 't_customer_user.grade'
	where cu.valid_flag = 0 and IF(#{data.roleId}='',1=1,roleId=#{data.roleId})
 ) a  