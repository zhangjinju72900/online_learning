select * from (
	select 
	a.id as id,
		ifnull(GROUP_CONCAT(DISTINCT tab1.region_id), '') as region,
	ifnull(tab2.role, '') as role,
	a.title as title,
	a.content as content,
	a.originator as originator,
	a.origin_time as originTime,
	a.release_status as releaseStatus,
	a.start_time as startTime,
	a.end_time as endTime,
	a.site as site,
	a.address as address,
	a.apply_count as applyCount,
	a.join_count as joinCount,
	a.activity_type as activityType,
	a.recommend_flag as reFlag,
	a.top_flag as tFlag,
	case 
	when   a.recommend_flag='0'   then '否'
    when   a.recommend_flag='1'   then '是' 
	end as recommendFlag,
	case 
	when   a.top_flag='0'   then '否'
    when   a.top_flag='1'   then '是' 
	end as topFlag,
	tab1.schoolId,
	cs.grade,
  GROUP_CONCAT(tab1.classId) as classId,
	GROUP_CONCAT(cs.`name`) as className,
  (select s.`name` from t_school s where s.id=tab1.schoolId) as schoolName,
	a.integral as integral,
	a.join_integral as joinIntegral,
	a.valid_flag as validFlag,
	a.update_time as updateTime,
	a.update_by as updateBy,
	a.create_by as createBy,
	a.create_time as createTime,
	a.file_id as fileId,
	a.qr_file_id as qrFileId,
	d.name as typeName,
	dd.name as releaseName,
	c.name as originatorName,
	ct.name as siteName,
	f.filename as file
	from t_activity a
	left join  t_activity_join_region tab1 on tab1.activity_id= a.id
	LEFT JOIN t_class cs on cs.id=tab1.classId
	left join (select activity_id, GROUP_CONCAT(role_id) as role from t_activity_join_role group by activity_id) tab2 on a.id= tab2.activity_id
	left join t_dict d on d.code = a.activity_type and d.cata_code = 't_activity.recommend_flag'
	left join t_dict dd on dd.code = a.release_status and dd.cata_code = 't_information.release_status'
	left join t_customer_user c on c.id = a.originator
	left join t_city ct on ct.code = a.site
	left join t_file_index f on f.id = a.file_id
	where a.valid_flag = 0 GROUP BY a.id
)a