INSERT INTO t_attend_class_record(school_id,
class_id,
course_id,
section_id,
student_count,
teacher_id,
valid_flag,
create_time,
create_by,
update_time,
update_by)
value (
#{data.schoolId},
#{data.classId},
#{data.courseId},
#{data.sectionId},
#{data.studentCount},
#{data.session.userInfo.empId},
1,
now(),
#{data.session.userInfo.empId},
now(),
#{data.session.userInfo.empId}
)