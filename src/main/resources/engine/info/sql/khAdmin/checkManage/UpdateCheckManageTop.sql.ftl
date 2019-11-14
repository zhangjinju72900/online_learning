update t_information set top_flag='0',
	top_time=null,
	update_time=NOW(),
	update_by=#{data.session.userInfo.userId} where #{data.tableName}='t_information' and id=#{data.id};
update t_information_review set top_flag=0 where #{data.tableName}='t_information_review' and id=#{data.id};
update t_live_review set top_flag=0 where #{data.tableName}='t_live_review' and id=#{data.id};