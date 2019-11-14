select * from (
		select 
	s.id as id,
	s.name as name,
	s.school_type,
	s.region_id as regid,
  s.province_code as provincecode,
  r.name as regionName,
  t.name as provinceName,
  s.create_by,
  s.update_by,
  c.name as empcreater,
  c2.name as empupdater,
  s.update_time as updateTime,
    s.create_time as createTime,
	school_type as type,
	
	(select count(1) from t_customer_user_role ur join t_customer_user u on ur.customer_user_id = u.id where ur.role_id = 10 and u.school_id = s.id and u.valid_flag = 0) as studentCount,
	(select count(1) from t_customer_user_role ur join t_customer_user u on ur.customer_user_id = u.id where ur.role_id = 11 and u.school_id = s.id and u.valid_flag = 0) as teacherCount,
	
	case 
	when   school_type='0'   then '中职'
    when   school_type='1'   then '高职'
	end as typeName
    from t_school s left join t_region r on s.region_id=r.id
    left join t_customer_user c on s.create_by=c.id
    left join t_customer_user c2  on s.update_by=c2.id
    left join (select code, name from t_city where level='1')t on t.code=s.province_code
WHERE s.valid_flag = 0
 ) a 