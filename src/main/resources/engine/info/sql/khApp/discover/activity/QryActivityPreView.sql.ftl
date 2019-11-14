select * from (
	select
       a.id,
       a.title,
       a.integral,
       a.join_integral as joinIntegral,
       a.originator,
       a.origin_time as originTime,
       a.start_time as startTime,
       a.site,
       a.content,
       a.address,
        case
              when a.start_time > now()
                then '-1'
              when now() >= a.start_time and a.end_time >= now()
                then '0'
              when now() >= a.end_time
                then '1'
       end as TimeType,
    a.review_count as reviewCount,
        a.like_count as likeCount,
       a.file_id as fileId,
       a.apply_count as applyCount,
       a.update_by as updateBy,
       a.create_by as createBy,
       a.update_time as updateTime,
       a.create_time as createTime,
       a.activity_type as activityType,
      case
        when (select ifnull (count(id),0) from t_activity_apply where activity_id = a.id and apply_by = #{data.session.userInfo.userId}) > 0 then '1'
       else '0'
     end as applyType,
     case
        when (select ifnull (count(id),0) from t_live_like where live_id = a.id and like_by = #{data.session.userInfo.userId}) > 0 then '1'
       else '0'
     end as likeType,
    u.name as uName,
    u.nickname as nickName,
    u.file_id as uFileId,
    r.roleId,
  	r.roleName,
  	ac.mu_url as muUrl
from t_activity a
       left join t_customer_user u on a.originator = u.id
      left join  (select ajr.activity_id, group_concat(ajr.role_id) as roleId, group_concat(tr.name) as roleName from
      	 t_activity_join_role ajr left join t_role tr on ajr.role_id = tr.id group by ajr.activity_id) r on a.id = r.activity_id
     left join t_activity_config ac on a.id = ac.activity_id 	 
where a.id = #{data.id} and a.valid_flag = 0
) a
