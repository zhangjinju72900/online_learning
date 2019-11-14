update t_customer_user set integral=integral-#{data.integral } 
where id=#{data.userId}
