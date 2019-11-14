update t_banner set valid_flag='1' ,update_time=now() ,update_by=#{data.session.userInfo.userId}
where id=#{primaryFieldValue} ;