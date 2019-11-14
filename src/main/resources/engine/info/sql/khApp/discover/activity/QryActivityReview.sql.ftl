select * from (
	SELECT
       lr.id,
       lr.live_id as liveId,
       lr.review_by as reviewBy,
       lr.review_time reviewTime,
       lr.content,
       lr.check_status as checkStatus,
       lr.check_time as checkTime,
       lr.check_by as checkBy,
       lr.top_flag as topFlag,
       lr.top_time as topTime,
       lr.top_by as topBy,
       lr.valid_flag as validFlag,
       lr.create_time as createTime,
       lr.create_by as createBy,
       lr.update_time as updateTime,
       u.file_id as uFileId,
       fi.oss_url as ufileOssUrl,
       lr.update_by as updateBy
from
       t_live_review lr
       left join t_customer_user u on lr.review_by = u.id
       left join t_file_index fi on u.file_id = fi.id
where lr.valid_flag = 0 and lr.check_status = 1 and lr.live_id = #{data.id}
order by lr.top_flag desc ,lr.top_time desc,lr.id desc
) a
