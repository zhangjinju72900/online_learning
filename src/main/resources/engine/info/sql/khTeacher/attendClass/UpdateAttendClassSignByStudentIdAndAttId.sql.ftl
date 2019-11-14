update t_attend_class_sign_record 
set valid_flag = 0, 
sign_type = 2, 
update_by = #{data.studentId},
 update_time = now()
 where attend_class_record_id = #{data.attendClassRecordId} 
 and student_id = #{data.studentId} and valid_flag = 1