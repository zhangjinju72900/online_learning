insert into t_maintain_manual_search_record(
maintain_manual_id,
content,
search_time,
school_id,
teacher_id,
valid_flag,
create_time,
create_by,
update_time,
update_by)
value (
#{data.id},
#{data.text},
now(),
(select school_id from t_customer_user where id = #{data.session.userInfo.userId}),
#{data.session.userInfo.userId},
0,
now(),
#{data.session.userInfo.empId},
now(),
#{data.session.userInfo.empId}
)