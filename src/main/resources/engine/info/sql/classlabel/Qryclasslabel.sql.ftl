select * from (
	select 
	l.id as id,
	l.name as labelname,
	l.show_order as showOrder,
	l.used_flag as usedFlag,
	d.name AS usedFlagName,
	l.create_time as createTime,
	l.create_by,
	l.update_time as updateTime,
	l.update_by,
    c.name as creater,
    c2.name as updater,
  IFNULL(tab.roleId, '') as roleId,
	IFNULL(tab.roleName, '') as rolename,
	l.label_type as typeId,
	case 
	when   label_type='0'   then '领域'
    when   label_type='1'   then '任务' 
	end as type
    from t_label l  
     left join t_customer_user c on l.create_by=c.id
    left join t_customer_user c2  on l.update_by=c2.id
    left join 
(select lr.label_id, GROUP_CONCAT(lr.role_id) as roleId, GROUP_CONCAT(r.`name`) as roleName from t_label_role lr  
    left join  t_role  r on  lr.role_id=r.id GROUP BY lr.label_id) tab on l.id = tab.label_id
    left join t_dict d ON d.code = l.used_flag AND d.cata_code = 't_label.used_flag' 
    where l.valid_flag = 0
 ) a 