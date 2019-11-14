select * from (
	SELECT
       ir.id,
       ir.subject_id as subjectId,
       ir.review_by as reviewBy,
       ir.review_time reviewTime,
       ir.content,
       ir.check_status as checkStatus,
       ir.check_time as checkTime,
       ir.check_by as checkBy,
       ir.top_flag as topFlag,
       ir.top_time as topTime,
       ir.top_by as topBy,
       ir.valid_flag as validFlag,
       ir.create_time as createTime,
       ir.create_by as createBy,
       ir.update_time as updateTime,
       u.file_id as uFileId,
       ir.update_by as updateBy
from
       t_subject_review ir
       left join t_customer_user u on ir.review_by = u.id
where ir.valid_flag = 0 and ir.check_status = 1 and ir.subject_id = #{data.id}
order by ir.top_flag desc ,ir.top_time desc,ir.id desc
) a
