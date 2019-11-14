select * from (
	select 
	f.id as id,
	f.feedback as feedback,
	f.feedback_by as feedbackBy,
	f.feedback_time as feedbackTime,
	f.create_time as createTime,
	f.create_by as createBy,
	f.update_time as updateTime,
	f.update_by as updateBy,
	s.name as schoolName,
	ifnull(cu.name,'') as feedbackName
	from t_feedback f
	left join t_customer_user cu on cu.id = f.feedback_by
	left join t_school s on s.id = cu.school_id
	where f.valid_flag = '0' and feedback  not like ""
 ) a 