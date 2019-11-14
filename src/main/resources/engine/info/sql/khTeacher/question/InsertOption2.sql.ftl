insert into t_teaching_assist_record (
 teacher_id,
 teacing_assist_id,
 profession_name,
 create_time,
 create_by
 )
values (
#{data.session.userInfo.userId},
#{data.assistId},
#{data.ProfessionalName},
now(),
#{data.session.userInfo.userId}
)