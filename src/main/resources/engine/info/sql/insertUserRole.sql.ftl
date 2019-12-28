insert into t_customer_user_role
(customer_user_id,role_id,update_time,update_by,create_time,create_by)
values (
#{data.userId} ,#{data.roleId} ,#{data.updateTime},#{data.updateBy},#{data.createTime},#{data.createBy}
)