update t_customer_user set integral=integral+#{data.integral },total_integral=total_integral+#{data.integral },update_time=now(),update_by=#{data.session.userInfo.userId},continuous_sign = continuous_sign+1
where id=#{data.userId}
