insert into t_class (
name,
class_type,
school_id, 
curriculum_id,
grade,
start_class_time,
valid_flag,
create_time,
create_by,
update_time,
update_by
)
values (
#{data.className},
(select code from t_dict where name = #{data.classType}),
#{data.schoolId},
#{data.curriculumId},
#{data.grade},
#{data.date},
0,
now(),
#{data.session.userInfo.userId},
now(),
#{data.session.userInfo.userId}
)