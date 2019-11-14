select * from (
SELECT
	ti.id as id,
	ti.title as title,
	ti.collect_count as collectCount, 
	ti.like_count as likeCount,
	ti.click_count as clickCount,
	ti.review_count as reviewCount, 
	
	case ti.valid_flag 
		when '0' then '正常'
		else '作废' 
	end as validFlag,
	
	case ti.source
		when '0' then '普通用户'
		when '1' then '官方'
		else '活动晒图'
	end as source,
	
	ti.release_time as releaseTime,
	ti.release_by as releaseBy,
	a.title as activityTitle,
    tab1.picId as picId,
    tab1.picId as fileId,
    tab1.fileOssUrl,
    case
        when (select ifnull (count(id),0) from t_information_like where information_id = ti.id and like_by = #{data.userId}) > 0 then '1'
    else '0'
     	end as likeType,
    ti.activity_id as activityId,
    a.file_id as activityPic 
FROM t_information ti 
left join t_activity a on ti.activity_id = a.id
left join (select ip.information_id, GROUP_CONCAT(ip.pic_id) as picId, GROUP_CONCAT(fi.oss_url) as fileOssUrl from t_information_pic ip left join t_file_index fi on ip.pic_id = fi.id GROUP BY ip.information_id) tab1 on ti.id = tab1.information_id
where ti.release_by = #{data.userId}
and ti.check_status = 1 and ti.valid_flag = 0 and ti.release_status = 1 and ti.source != 1

union all

SELECT
	ti.id as id,
	ti.title as title,
	ti.collect_count as collectCount, 
	ti.like_count as likeCount,
	ti.click_count as clickCount,
	ti.review_count as reviewCount, 
	
	case ti.valid_flag 
		when '0' then '正常'
		else '作废' 
	end as validFlag,
	
	case ti.source
		when '0' then '普通用户'
		when '1' then '官方'
		else '活动晒图'
	end as source,
	
	ti.release_time as releaseTime,
	ti.release_by as releaseBy,
	'' as activityTitle,
    ti.file_id as picId,
    ti.file_id as fileId,
    fi.oss_url as fileOssUrl,
    case
        when (select ifnull (count(id),0) from t_information_like where information_id = ti.id and like_by = #{data.userId}) > 0 then '1'
    else '0'
     	end as likeType,
    ti.activity_id as activityId,
    ti.file_id as activityPic 
FROM t_information ti 
 left join t_file_index fi on ti.file_id = fi.id
where ti.release_by = #{data.userId}
and ti.check_status = 1 and ti.valid_flag = 0 and ti.release_status = 1 and ti.source = 1

 ) a 
