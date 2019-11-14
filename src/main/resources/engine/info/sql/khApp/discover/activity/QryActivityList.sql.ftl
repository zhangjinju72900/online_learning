select * from (
	SELECT
       a.id,
       a.title,
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
       case
              when a.end_time < now()
                then '1'
                else '0'
       end as endTimeType,
       a.site,
       a.address,
       a.file_id as fileId,
	   fi.oss_url as fileOssUrl,
       a.top_time,
       a.top_flag,
       a.activity_type as activityType,
       u.name as uName,
       u.file_id as uFileId,
       fi2.oss_url as ufileOssUrl
from
       t_activity a
       		left join t_file_index fi on a.file_id = fi.id
		    left join t_customer_user u on a.create_by = u.id
		    left join t_file_index fi2 on u.file_id = fi2.id
where a.valid_flag = 0 and a.release_status = 1
order by a.start_time desc,a.top_flag desc ,a.top_time desc,a.origin_time desc
) a
