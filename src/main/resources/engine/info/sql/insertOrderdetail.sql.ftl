insert into t_order_detail (order_id, good_id, good_name, quantity, integral, amount, create_time, create_by,update_time,update_by)
values (#{data.ddbh},#{data.goodId},#{data.goodsname},#{data.Quantity},#{data.Integral},#{data.Amount},now(),#{data.userid},now(),#{data.userid})
