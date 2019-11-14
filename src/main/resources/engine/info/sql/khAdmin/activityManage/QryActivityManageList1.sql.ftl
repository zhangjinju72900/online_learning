select * from (
		select 
	a.id as id,
	ifnull(tab1.region, '') as region,
	ifnull(tab2.role, '') as role,
	a.title as title,
	a.content as content,
	a.originator as originator,
	date(a.origin_time) as originTime,
	a.release_status as releaseStatus,
	a.start_time as startTime,
	a.end_time as endTime,
	a.site as site,
	a.address as address,
	case when a.apply_count is null then 0 else a.apply_count end applyCount,
	case when a.join_count  is null then 0 else a.join_count  end joinCount,
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
	f.filename as file,
	case when (select count(1) from t_information n where n.activity_id=a.id and n.check_status='1') is null  then 0 else (select count(1) from t_information n where n.activity_id=a.id and n.check_status='1') end joinPicCount
	from t_activity a
	left join (select activity_id, GROUP_CONCAT(DISTINCT region_id) as region from t_activity_join_region group by activity_id) tab1 on a.id= tab1.activity_id
	left join (select activity_id, GROUP_CONCAT(DISTINCT role_id) as role from t_activity_join_role group by activity_id) tab2 on a.id= tab2.activity_id
	left join t_dict d on d.code = a.activity_type and d.cata_code = 't_activity.recommend_flag'
	left join t_dict dd on dd.code = a.release_status and dd.cata_code = 't_information.release_status'
	left join t_customer_user c on c.id = a.originator
	left join t_city ct on ct.code = a.site
	left join t_file_index f on f.id = a.file_id
	where a.valid_flag = 0  and (a.create_by = #{data.userInfo.userId} or #{data.userInfo.userId}='2')
)a