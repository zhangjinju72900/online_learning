insert into t_order_detail 
(order_id, good_id, good_name, quantity, integral, amount, create_time, create_by,update_time,update_by)
select 
	#{data.orderId},
	o.id,
	o.`name`,
#{data.Quantity},#{data.Integral},#{data.Amount},now(),#{data.session.userInfo.userId},now(),#{data.session.userInfo.userId}
from t_goods o where o.id=#{data.goodsId}