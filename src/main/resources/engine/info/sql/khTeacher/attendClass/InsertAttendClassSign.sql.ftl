INSERT INTO t_attend_class_sign_record(attend_class_record_id,
school_id,
student_id,
sign_time,
valid_flag,
create_time,
create_by,
update_time,
update_by)
value (
#{data.attendClassRecordId},
#{data.schoolId},
#{data.studentId},
now(),
1,
now(),
#{data.session.userInfo.empId},
now(),
#{data.session.userInfo.empId}
)