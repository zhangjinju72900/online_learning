
 select case when a.sumFlag < a.countFlag then 0 else 1 end readFlag,
case when send_type = 0 || send_type = 2
                       then 1
               when send_type = 1 || send_type = 3
                       then 2
               when send_type = 9
                       then 3
               when send_type = 10
                       then 4
else a.send_type end
  senderType from
(select 
	sum(t.read_flag) as sumFlag,
	count(t.read_flag) as countFlag,
	t.send_type
 from t_message_record t where t.receiver_id=#{data.session.userInfo.userId} GROUP BY t.send_type)a