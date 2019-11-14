select * from (
SELECT
	tn.id as id,
	tn.title as title,
	tn.context as context,
	tn.release_time as releaseTime,
	tn.release_by as releaseBy,
	tn.update_time as updateTime,
	tn.update_by as updateBy,
	ifnull(tn.release_status,'') as releaseStatus,
	ifnull(tnr.role_id,'') as role,
	ifnull(tnre.region_id,'') as area,
	f.file_id as fileId,
    CONCAT(fn.filename ,'.',fn.file_type)	as fileName
FROM t_notice tn
LEFT JOIN t_notice_file f on f.notice_id=tn.id
LEFT JOIN t_file_index fn on fn.id=f.file_id
left join (select notice_id, GROUP_CONCAT(role_id) as role_id from t_notice_user_role group by notice_id) tnr on tnr.notice_id=tn.id
left join (select notice_id, GROUP_CONCAT(region_id) as region_id from t_notice_region group by notice_id) tnre on tnre.notice_id=tn.id




 ) a 
