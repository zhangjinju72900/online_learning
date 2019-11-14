update t_attend_class_sign_record
set valid_flag = 0, update_time = now(), update_by = #{data.session.userInfo.userId}
where id = #{data.id}
and attend_class_record_id = #{data.attendClassRecordId} and valid_flag = 1