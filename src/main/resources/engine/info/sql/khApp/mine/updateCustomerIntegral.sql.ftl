update t_customer_user set integral=integral+#{data.integral },total_integral=total_integral+#{data.integral },update_time=now(),update_by=#{data.session.userInfo.userId}
where id=#{data.userId}
