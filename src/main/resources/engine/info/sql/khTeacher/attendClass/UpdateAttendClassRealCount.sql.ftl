update t_attend_class_record set real_student_count = real_student_count+1,
 update_by = #{data.studentId}, update_time = now()
 where id = #{data.attendClassRecordId} and 
 not EXISTS (
select id from t_attend_class_sign_record 
where attend_class_record_id = #{data.attendClassRecordId} 
and student_id = #{data.studentId} and valid_flag = 0)