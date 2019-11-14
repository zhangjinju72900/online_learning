update t_attend_class_record
set end_time = now(), update_time = now(), update_by = #{data.session.userInfo.userId}
where id = #{data.attendClassRecordId}