
 select * from (
SELECT
	tor.id as id,
	tor.user_id as userId,
	tor.code as code,
	tor.order_time as orderTime,
		tor.order_time ,
	tor.order_status as orderStatus,
	tor.logistic as logistic ,
	tor.logistic_code as logisticCode,
	case when tor.logistic_code!='' and tor.order_status=1  then '配送中' else ''  end statusName,
	tod.id as goodId,
	tod.name as goodName,
	tod.quantity as quantity,
	tor.integral AS integral,
	tor.amount,
	tor.real_amount AS realAmount,
	tor.real_integral AS realIntegral,
    case when (tgp.pic_id is not null) then SUBSTRING_INDEX(tgp.pic_id,',',1) else '' end picId
	FROM t_order tor
JOIN t_order_detail d on d.order_id=tor.id
JOIN t_goods tod on tod.id=d.good_id
LEFT JOIN t_goods_pic tgp on tod.id=tgp.good_id where tor.order_status!=9 and  tor.user_id=#{data.userId} GROUP BY tor.id ORDER BY order_time desc
 ) a 