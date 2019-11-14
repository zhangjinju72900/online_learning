insert into t_curriculum (
name,
school_id,
execute_time,
professional_id,
valid_flag,
create_time,
create_by,
update_time,
update_by,
uuid
)
values (
#{data.content},
#{data.schoolId},
#{data.date},
#{data.professionalId},
0,
now(),
#{data.session.userInfo.userId},
now(),
#{data.session.userInfo.userId},
#{data.uuid}
)