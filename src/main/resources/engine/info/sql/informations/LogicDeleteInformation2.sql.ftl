update t_information set
valid_flag=1,
update_time=now(),
create_by=#{data.session.userInfo.userId}
where id=#{data.id} and release_by = #{data.userId}
