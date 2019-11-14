select * from (
SELECT
	tn.id as id,
	tn.title as title,
	tn.context as context,
	tn.release_time as releaseTime,
	tn.release_by as releaseBy,
	tn.update_time as updateTime,
	tn.update_by as updateBy,
	tu.name as releaseByna,
	tuu.name as updateByna,
	ifnull(tn.release_status,'') as releaseStatusNo,
	d.name as releaseStatus,
	ifnull(tab1.role_id,'') as roleId,
	tab1.roleName as role,
	ifnull(tab2.region_id,'') as regionId,
	tab2.regionName as area
FROM t_notice tn
left join t_dict d on d.code = tn.release_status and d.cata_code = 't_information.release_status'
left join t_customer_user tu on tu.id=tn.release_by
left join t_customer_user tuu on tuu.id=tn.update_by
left join (select tnr.notice_id, GROUP_CONCAT(tnr.role_id) as role_id, tr.name as roleName from t_notice_user_role tnr 
left join t_role tr on tr.id=tnr.role_id GROUP BY tnr.notice_id)tab1 on tab1.notice_id=tn.id
left join (select tnre.notice_id, GROUP_CONCAT(region_id) as region_id, GROUP_CONCAT(tre.name)as regionName from t_notice_region tnre 
left join t_region tre on tre.id=tnre.region_id GROUP BY tnre.notice_id)tab2 on tab2.notice_id=tn.id
	where tn.valid_flag = '0' 


 ) a 
