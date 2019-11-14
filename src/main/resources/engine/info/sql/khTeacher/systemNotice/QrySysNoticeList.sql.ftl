select * from (
SELECT 
	tmr.id AS id,
	tmr.receiver_id AS receiverId,
	tmr.sender_id AS senderId,
	tmr.send_type AS sendType,
	tmr.send_time AS sendTime,
	tmr.base_id AS baseId,
	tn.title AS title,
	tn.context AS context,
	tn.release_time AS releaseTime,
	tmr.read_flag AS readFlag,
	case when tmr.read_flag=0 then '未读' else '已读' end readFlagName,
	tmr.valid_flag AS validFlag,
	tmr.remark AS remark 
FROM t_message_record tmr
LEFT JOIN t_notice tn ON tmr.base_id = tn.id 
WHERE tmr.send_type = 20 AND tmr.receiver_id = #{data.session.userInfo.userId} and tmr.valid_flag=0 
 ) a