update t_information set valid_flag=1 where #{data.tableName}='t_information' and id=#{data.id};
update t_information_review set valid_flag=1 where #{data.tableName}='t_information_review' and id=#{data.id};
update t_live_review set valid_flag=1 where #{data.tableName}='t_live_review' and id=#{data.id};