select * from (
	 SELECT 
	 CASE 
	 	WHEN customer_user_id='data.customerUserId' AND focus_on_id='focusOnId' then 1 
	 	ELSE 0 
	 	END AS sub,
	 	o.id AS id,
		o.customer_user_id AS customerUserId,
		o.focus_on_id AS focusOnId,
		u.id AS customerId,
		u.name AS nickName,
		u.file_id AS fileId 
	 FROM t_user_focus_on o
	 LEFT JOIN t_customer_user u ON o.focus_on_id=u.id
	 where o.valid_flag = '0'
) a 