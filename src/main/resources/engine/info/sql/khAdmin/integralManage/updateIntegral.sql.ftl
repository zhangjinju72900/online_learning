update t_customer_user set integral=integral+#{data.Entegral }
where id=#{data.userId}
