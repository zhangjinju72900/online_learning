select * from (
SELECT
	tn.id as id,
	tn.notice_id as noticeId,
	tn.school_id as schoolId,
	s.name as schoolName,
	tn.create_time as createTime,
	tn.create_by as createBy,
	tn.update_time as updateTime,
	tn.update_by as updateBy
	
FROM t_notice_school tn
left join t_school s on tn.school_id=s.id
	where tn.notice_id = #{data.id}



 ) a 
