select * from (
SELECT 
	tn.title AS title,
	tn.context AS context,
	tn.release_time AS releaseTime,
	CONCAT(tc.grade,"级",tc.name) AS className,
	tmr.valid_flag AS validFlag
FROM t_message_record tmr
LEFT JOIN t_notice tn ON tmr.base_id = tn.id 
LEFT JOIN t_customer_user_class tcuc ON tcuc.customer_user_id = tmr.receiver_id 
LEFT JOIN t_class tc ON tc.id = tcuc.class_id 
WHERE tmr.send_type=30 AND tmr.sender_id = #{data.receiverId} AND tmr.valid_flag='0'
ORDER BY releaseTime DESC
 ) a