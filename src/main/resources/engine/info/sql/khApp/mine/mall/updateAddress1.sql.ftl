update t_consign_address set is_default=1 where user_id=#{data.userId} and is_default=0