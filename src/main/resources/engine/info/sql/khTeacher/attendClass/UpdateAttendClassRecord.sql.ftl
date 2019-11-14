update t_attend_class_record
set valid_flag         = 0, start_time = now(), update_time = now(), update_by = #{data.session.userInfo.userId},
real_student_count =(select count(1) from t_attend_class_sign_record where attend_class_record_id=#{data.attendClassRecordId} and valid_flag='0')
where id = #{data.attendClassRecordId}