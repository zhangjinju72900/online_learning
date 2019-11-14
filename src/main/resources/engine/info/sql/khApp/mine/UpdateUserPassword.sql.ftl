update t_customer_user set password=#{data.newPassword}
where id=#{data.userId}
