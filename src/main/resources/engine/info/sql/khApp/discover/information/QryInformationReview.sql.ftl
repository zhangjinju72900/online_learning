select * from (
	SELECT
       ir.id,
       ir.information_id as infoId,
       ir.review_by as reviewBy,
       u.nickname as reviewName,
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
       fi.oss_url as ufileOssUrl,
       ir.update_by as updateBy,
       case il.like_by when #{data.session.userInfo.userId} then '已点赞'
	   else '未点赞'
	   end as likeValidName,
	   case il.like_by when #{data.session.userInfo.userId} then '1'
	   else '0'
	   end as likeValid
	   from
       t_information_review ir 
       left join t_customer_user u on ir.review_by = u.id 
       left join t_file_index fi on u.file_id = fi.id
       LEFT JOIN (select DISTINCT review_id, like_by from t_information_like where information_id = #{data.id} and like_by = #{data.session.userInfo.userId}) il
	   on ir.id = il.review_id
where ir.valid_flag = 0 and ir.check_status = 1 and ir.review_id = 0 and ir.information_id = #{data.id}
order by ir.top_flag desc ,ir.top_time desc,ir.id desc
) a
