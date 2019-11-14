insert into t_customer_user_class (class_id,
customer_user_id,
create_time,
create_by,
update_time,
update_by)
values (
#{data.classId},
#{data.userId},
now(),
#{data.session.userInfo.userId},
now(),
#{data.session.userInfo.userId}
)