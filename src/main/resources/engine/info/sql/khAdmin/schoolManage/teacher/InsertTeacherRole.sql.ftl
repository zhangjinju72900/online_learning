insert into t_customer_user_role (
role_id,
customer_user_id,
create_time,
create_by,
update_time,
update_by)
values (
11,
#{data.userId},
now(),
#{data.session.userInfo.userId},
now(),
#{data.session.userInfo.userId}
)