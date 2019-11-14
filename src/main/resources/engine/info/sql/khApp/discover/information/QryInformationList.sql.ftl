	select * from (
	    SELECT
            i.id,
            i.title, 
            i.collect_count as collectCount, 
            i.like_count as likeCount, 
            i.click_count as clickCount, 
            i.review_count as count, 
            u.name as releaseName, 
            i.file_id as fileId,
            fi.oss_url as fileOssUrl,
            i.source,
            i.release_time as releaseTime,
            i.valid_flag,
            i.release_by as releaseBy,
            i.create_time as createTime,
            i.create_by as createBy,
            i.update_by as updateBy,
            i.update_time as updateTime,
            i.top_time,
            i.top_flag,
            'info' as type,
            case
		        when (select ifnull (count(id),0) from t_information_collect where information_id = i.id and collect_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as collectType,
		    case
		        when (select ifnull (count(id),0) from t_information_like where information_id = i.id and like_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as likeType,
		    u.nickname,
		    case when ufo.focus_on_id is null
		   		then '0'
		   	else '1' 
		   		end as focusType,
		    i.release_address as address,
		    u.file_id as uFileId,
		    fi2.oss_url as ufileOssUrl,
		    '' as activityTitle,
	        '' as activityPic,
	        '' as activityId  	 	
        from
            t_information i
            left join t_file_index fi on i.file_id = fi.id
            left join (select focus_on_id from t_user_focus_on where customer_user_id = #{data.session.userInfo.userId} and valid_flag = '0' group by focus_on_id) ufo on i.release_by = ufo.focus_on_id
			left join t_customer_user u on i.release_by = u.id
			left join t_file_index fi2 on u.file_id = fi2.id
        where i.valid_flag = 0 and i.release_status = 1 and i.check_status = 1 and i.source = 1
    ) a
    union all

	select * from (
	    SELECT
            i.id,
            i.title, 
            i.collect_count as collectCount, 
            i.like_count as likeCount, 
            i.click_count as clickCount, 
            i.review_count as count, 
            u.name as releaseName, 
            case when i.file_id = -1 then i.file_id else 
            tab1.fileId end as fileId, 
            tab1.fileOssUrl,
            i.source, 
            i.release_time as releaseTime,
            i.valid_flag,
            i.release_by as releaseBy,
            i.create_time as createTime,
            i.create_by as createBy,
            i.update_by as updateBy,
            i.update_time as updateTime,
            i.top_time,
            i.top_flag,
            'info' as type,
            case
		        when (select ifnull (count(id),0) from t_information_collect where information_id = i.id and collect_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as collectType,
		    case
		        when (select ifnull (count(id),0) from t_information_like where information_id = i.id and like_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as likeType,
		    u.nickname,
		    case when ufo.focus_on_id is null
		   		then '0'
		   	else 1 
		   		end as focusType,
		    i.release_address as address,
		    u.file_id as uFileId,
		    fi2.oss_url as ufileOssUrl,
		    a.title as activityTitle,
	        a.file_id as activityPic,
	        i.activity_id as activityId 	
        from
            t_information i 
            left join (select GROUP_CONCAT(ip.pic_id) as fileId, ip.information_id, GROUP_CONCAT(fi.oss_url) as fileOssUrl from t_information_pic ip left join t_file_index fi on ip.pic_id = fi.id group by ip.information_id) tab1 on i.id = tab1.information_id
            left join (select focus_on_id from t_user_focus_on where customer_user_id = #{data.session.userInfo.userId} and valid_flag = '0' group by focus_on_id) ufo on i.release_by = ufo.focus_on_id
			left join t_customer_user u on i.release_by = u.id
			left join t_file_index fi2 on u.file_id = fi2.id
			left join t_activity a on i.activity_id = a.id
        where i.valid_flag = 0 and i.release_status = 1 and i.check_status = 1 and i.source != 1
    ) a
    union all
    select * from (
	    SELECT
            s.id,
            s.title, 
            0 as collectCount, 
            0 as likeCount, 
            s.click_count as clickCount,
            s.review_count as count, 
            u.name as releaseName, 
            s.file_id as fileId, 
            fi.oss_url as fileOssUrl,
            '' as source, 
            s.release_time as releaseTime,
            s.valid_flag,
            s.release_by as releaseBy,
            s.create_time as createTime,
            s.create_by as createBy,
            s.update_by as updateBy,
            s.update_time as updtaeTime,
            s.top_time,
            s.top_flag,
            'sub' as type,
            '0' as collectType,
            '0' as likeType,
            u.nickname,
            '0' as focusType,
            '' as address,
            u.file_id as uFileId,
            fi2.oss_url as ufileOssUrl,
		    '' as activityTitle,
	        '' as activityPic,
	        '' as activityId 
        from
            t_subject s
            left join t_file_index fi on s.file_id = fi.id
			left join t_customer_user u on s.release_by = u.id
			left join t_file_index fi2 on u.file_id = fi2.id
        where s.valid_flag = 0 and s.release_status = 1    
    ) a order by top_flag desc ,top_time desc,releaseTime desc
