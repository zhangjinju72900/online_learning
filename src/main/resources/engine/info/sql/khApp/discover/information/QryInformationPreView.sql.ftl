select * from(
select
  i.id,
  i.title,
  u.name as releaseName,
  i.content,
  i.source,
  i.file_id as fileId,
  fi.oss_url as fileOssUrl,
  i.like_count as likeCount,
  i.review_count as reviewCount,
  i.review_count as count, 
  i.collect_count as collectCount,
  i.create_time as createTime,
  i.create_by as createBy,
  i.release_by as releaseBy,
  i.information_label_id as informationLabelId,
  i.latitude,
  i.release_address as relesesAddress,
  i.longitude,
  i.release_time releaseTime,
  u.file_id as uFileId,
  fi2.oss_url as ufileOssUrl,
  u.nickname,
   	case
		        when (select ifnull (count(id),0) from t_information_collect where information_id = i.id and collect_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as collectType,
	case
		        when (select ifnull (count(id),0) from t_information_like where information_id = i.id and like_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as likeType,
    case when ufo.focus_on_id is null
		   		then '0'
		   	else '1' 
		   		end as focusType,
	'' as activityTitle,
    '' as activityPic,
    '' as activityId  	   			     		     	
from t_information i
	left join t_file_index fi on i.file_id = fi.id
	left join t_customer_user u on i.release_by = u.id
	left join t_file_index fi2 on u.file_id = fi2.id
	left join (select focus_on_id from t_user_focus_on where customer_user_id = #{data.session.userInfo.userId} and valid_flag = '0' group by focus_on_id) ufo on i.release_by = ufo.focus_on_id
where i.id = #{data.id} and i.source = 1 and i.valid_flag = 0 and i.release_status = 1 and i.check_status = 1

union all

select
  i.id,
  '' as title,
  u.name as releaseName,
  i.content,
  i.source,
  tab1.fileId,
  tab1.ossUrl as fileOssUrl,
  i.like_count as likeCount,
  i.review_count as reviewCount,
  i.review_count as count, 
  i.collect_count as collectCount,
  i.create_time as createTime,
  i.create_by as createBy,
  i.release_by as releaseBy,
  i.information_label_id as informationLabelId,
  i.latitude,
  i.release_address as relesesAddress,
  i.longitude,
  i.release_time releaseTime,
  u.file_id as uFileId,
  fi2.oss_url as ufileOssUrl,
  u.nickname,
  case
		        when (select ifnull (count(id),0) from t_information_collect where information_id = i.id and collect_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as collectType,
  case
		        when (select ifnull (count(id),0) from t_information_like where information_id = i.id and like_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as likeType,
  case when ufo.focus_on_id is null
		   		then '0'
		   	else '1' 
		   		end as focusType,
  a.title as activityTitle,
  a.file_id as activityPic,
  i.activity_id as activityId	   				     		     	
from t_information i 
	left join (select focus_on_id from t_user_focus_on where customer_user_id = #{data.session.userInfo.userId} and valid_flag = '0' group by focus_on_id) ufo on i.release_by = ufo.focus_on_id
	left join (select GROUP_CONCAT(ip.pic_id) as fileId, GROUP_CONCAT(fi.oss_url) as ossUrl, ip.information_id from t_information_pic ip left join t_file_index fi on ip.pic_id = fi.id where information_id = #{data.id}) tab1 on i.id = tab1.information_id
	left join t_customer_user u on i.release_by = u.id
	left join t_file_index fi2 on u.file_id = fi2.id
	left join t_activity a on i.activity_id = a.id
where i.id = #{data.id} and i.source != 1 and i.valid_flag = 0 and i.release_status = 1 and i.check_status = 1

)a