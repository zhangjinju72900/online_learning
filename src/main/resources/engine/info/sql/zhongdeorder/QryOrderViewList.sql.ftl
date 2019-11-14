select * from (
SELECT
	tor.id as id,
	tca.name as consignee,
	tca.tel as tel,
	CONCAT(tca.address,' ',tca.address_detail) as address,
	tab1.good_id as goodId,
	tab1.good_name as goodName,
	tab1.quantity as quantity,
	tab1.specification as specification,
	tab1.pic_id as picId,
	tor.order_time as orderTime,
	tor.code as code,
	tor.order_status AS orderStatus,
	tor.integral,
	tor.amount,	
	tor.real_amount AS realAmount,
	tor.real_integral AS realIntegral,	
	CASE
		WHEN tor.order_status = 0 THEN
			''
		ELSE
			concat(real_integral,'积分 + ',real_amount,'元')
		END AS totalAmount,
	tor.pay_method AS payMethod,
	tor.logistic_code AS logisticCode,
	tor.logistic AS logistic,
	tor.logistic_amount as logisticAmount,
	cu.integral as usableIntegral
FROM t_order tor
LEFT JOIN (select id, name, tel, address_detail,address from t_consign_address  where valid_flag = '0') tca on tca.id=tor.consign_address_id
LEFT JOIN (select tod.order_id, GROUP_CONCAT(tod.good_id) as good_id, GROUP_CONCAT(tod.good_name) as good_name, GROUP_CONCAT(tod.quantity) as quantity, 
	GROUP_CONCAT(tod.specification) as specification, GROUP_CONCAT(tgp.pic_id) as pic_id from t_order_detail tod 
LEFT JOIN (select good_id, GROUP_CONCAT(pic_id) as pic_id from t_goods_pic GROUP BY good_id) tgp on tgp.good_id=tod.good_id group by tod.order_id)tab1 on tab1.order_id=tor.id
left join t_customer_user cu on tor.user_id = cu.id
where  tor.id = #{data.id} 
 ) a 