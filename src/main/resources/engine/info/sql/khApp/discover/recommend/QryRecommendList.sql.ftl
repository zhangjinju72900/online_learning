select * from (
select * from (
	    SELECT
            i.id,
            i.file_id as fileId,
            fi.oss_url as fileOssUrl,
            i.source,
            i.title,
            i.collect_count as collectCount, 
            i.like_count as likeCount, 
            i.click_count as clickCount,
            i.review_count as reviewCount,
            i.release_time as releaseTime,
            i.release_by as releaseBy,
            i.valid_flag,
            i.create_by as createBy,
            i.create_time as createTime,
            i.top_time,
            i.top_flag,
            'info' as type,
            u.name as uName,
            u.file_id as uFileId,
            fi2.oss_url as ufileOssUrl,
        	case
		        when (select ifnull (count(id),0) from t_information_like where information_id = i.id and like_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as likeType,
		    case
		        when (select ifnull (count(id),0) from t_information_collect where information_id = i.id and collect_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as collectType, 	
		    u.nickname,
		    case when ufo.focus_on_id is null
		   		then '0'
		   	else '1' 
		   		end as focusType,
		    i.release_address as address,
		    '' as startTime,
	        '' as timeType,
	        '' as endTime,
	        '' as activityTitle,
	        '' as activityPic,
	        '' as activityId 	
        from
            t_information i
            	left join t_file_index fi on i.file_id = fi.id
            	left join (select focus_on_id from t_user_focus_on where customer_user_id = #{data.session.userInfo.userId} and valid_flag = '0' group by focus_on_id) ufo on i.release_by = ufo.focus_on_id
                left join t_customer_user u on i.release_by = u.id
                left join t_file_index fi2 on u.file_id = fi2.id

        where i.valid_flag = 0 and i.release_status = 1 and i.check_status = 1 and i.recommend_flag = 1 and i.source = 1
    ) a
    union all
    
    select * from (
	    SELECT
            i.id,
            case when i.file_id = -1 then i.file_id else 
            tab1.fileId end as fileId, 
            tab1.fileOssUrl,
            i.source,
            i.title, 
            i.collect_count as collectCount, 
            i.like_count as likeCount, 
            i.click_count as clickCount,
            i.review_count as reviewCount,
            i.release_time as releaseTime,
            i.release_by as releaseBy,
            i.valid_flag,
            i.create_by as createBy,
            i.create_time as createTime,
            i.top_time,
            i.top_flag,
            'info' as type,
            u.name as uName,
            u.file_id as uFileId,
            fi2.oss_url as ufileOssUrl,
        	case
		        when (select ifnull (count(id),0) from t_information_like where information_id = i.id and like_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as likeType,
		    case
		        when (select ifnull (count(id),0) from t_information_collect where information_id = i.id and collect_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as collectType, 	
		    u.nickname, 	
		    case when ufo.focus_on_id is null
		   		then '0'
		   	else '1' 
		   		end as focusType,
		    i.release_address as address,
		    '' as startTime,
	        '' as timeType,
	        '' as endTime,
	        a.title as activityTitle,
	        a.file_id as activityPic,
	        i.activity_id as activityId 	
        from
            t_information i 
            	left join (select focus_on_id from t_user_focus_on where customer_user_id = #{data.session.userInfo.userId} and valid_flag = '0' group by focus_on_id) ufo on i.release_by = ufo.focus_on_id
                left join t_customer_user u on i.release_by = u.id
                left join t_file_index fi2 on u.file_id = fi2.id
				left join (select GROUP_CONCAT(ip.pic_id) as fileId, ip.information_id, GROUP_CONCAT(fi.oss_url) as fileOssUrl from t_information_pic ip left join t_file_index fi on ip.pic_id = fi.id group by ip.information_id) tab1 on i.id = tab1.information_id
				left join t_activity a on i.activity_id = a.id
        where i.valid_flag = 0 and i.release_status = 1 and i.check_status = 1 and i.recommend_flag = 1 and i.source != 1
    ) a
    union all
    
    select * from (
	    SELECT
            s.id,
            s.file_id as fileId, 
            fi.oss_url as fileOssUrl,
            0 as source, 
            s.title, 
            0 as collectCount, 
            0 as likeCount,
            s.click_count as clickCount,
            s.review_count as reviewCount,
            s.release_time as releaseTime,
            s.release_by as releaseBy,
            s.valid_flag,
            s.create_by as createBy,
            s.create_time as createTime,
            s.top_time,
            s.top_flag,'sub' as type,
            u.name as uName,
            u.file_id as uFileId,
            fi2.oss_url as ufileOssUrl,
            0 as likeType,
            0 as collectType,
            u.nickname,  	
		    0 as focusType,
		   '' as address,
		   '' as startTime,
	       '' as timeType,
	       '' as endTime,
	       '' as activityTitle,
	       '' as activityPic,
	       '' as activityId 
        from
            t_subject s
				left join t_file_index fi on s.file_id = fi.id
                left join t_customer_user u on s.release_by = u.id
                left join t_file_index fi2 on u.file_id = fi2.id
        where s.valid_flag = 0 and s.release_status = 1 and s.recommend_flag = 1
    )b
    union all
      select * from (
	    SELECT
       a.id,
       a.file_id as fileId, 
       fi.oss_url as fileOssUrl,
       0 as source, 
       a.title, 
       0 as collectCount, 
       a.like_count as likeCount, 
       a.join_count as clickCount,
       a.review_count as reviewCount,
       a.origin_time as releaseTime,
       a.originator as releaseBy,
       a.valid_flag,
       a.create_by as createBy,
       a.create_time as createTime,
       a.top_time,
       a.top_flag,
       'act' as type,
       u.name as uName,
       u.file_id as uFileId,
       fi2.oss_url as ufileOssUrl,
       0 as likeType,
       0 as collectType,
       u.nickname,	
	   '0' as focusType,
	   a.address as address,
	   a.start_time as startTime,
       case
              when a.start_time > now()
                then '-1'
              when now() >= a.start_time and a.end_time >= now()
                then '0'
              when now() >= a.end_time
                then '1'
       end as timeType,
       a.end_time as endTime,
       '' as activityTitle,
       '' as activityPic,
       '' as activityId 
from
       t_activity a
       			left join t_file_index fi on a.file_id = fi.id
                left join t_customer_user u on a.originator = u.id
				left join t_file_index fi2 on u.file_id = fi2.id
              where a.valid_flag = 0 and a.release_status = 1 and a.recommend_flag = 1
    )c order by top_flag desc ,top_time desc,releaseTime desc
    )d
