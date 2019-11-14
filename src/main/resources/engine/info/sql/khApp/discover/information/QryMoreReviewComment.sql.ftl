select * from (
	SELECT
       ir.id as id,
       ir.information_id as infoId,
       ir.review_by as reviewBy,
       ir.review_id as reviewId,
       u.nickname as reviewName,
       ir.review_time reviewTime,
       ir.content as content,
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
       ir.update_by as updateBy,
       case il.like_by when #{data.session.userInfo.userId} then '已点赞'
	   else '未点赞'
	   end as likeValidName,
	   case il.like_by when #{data.session.userInfo.userId} then '1'
	   else '0'
	   end as likeValid
	   from
       t_information_review ir left join t_customer_user u on ir.review_by = u.id
       LEFT JOIN t_information_like il
	   on ir.id = il.review_id
	where ir.valid_flag = 0 and ir.check_status = 1   and ir.review_id <> 0  and ir.review_id = #{data.reviewId}
		order by ir.top_flag desc ,ir.top_time desc,ir.id desc
) a 
