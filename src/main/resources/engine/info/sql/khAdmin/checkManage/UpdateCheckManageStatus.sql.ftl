update t_live_review set check_status=1,check_time=now(),check_by=#{data.session.userInfo.userId} where #{data.type}='live_review' and id = #{data.id};
update t_information set check_status=1,check_time=now(),check_by=#{data.session.userInfo.userId} where #{data.type}='info' and id = #{data.id};
update t_information_review set check_status=1,check_time=now(),check_by=#{data.session.userInfo.userId} where #{data.type}='info_review' and id = #{data.id};
update t_information set check_status=1,check_time=now(),check_by=#{data.session.userInfo.userId} where #{data.type}='ac_join_pic' and id = #{data.id};
update t_information_review set check_status=1,check_time=now(),check_by=#{data.session.userInfo.userId} where #{data.type}='info_review_review' and id = #{data.id};