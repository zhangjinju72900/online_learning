insert into
	t_customer_user
	(name,card_num,tel,sex,school_id,grade,valid_flag,user_explain,uuid,create_time,create_by,update_time,update_by,password)
	values
	(#{data.name},#{data.cardNum},#{data.tel},#{data.sex},#{data.schoolId},#{data.grade},0,#{data.userExplain},#{data.uuid},now(),#{data.session.userInfo.userId},now(),#{data.session.userInfo.userId},#{data.password});