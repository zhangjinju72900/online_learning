select o.id,o.order_status as orderStatus,o.logistic_amount,o.amount,o.integral,d.good_id as goodsId,d.good_name as goodsName,o.create_by as userId from t_order o left JOIN t_order_detail d on d.order_id=o.id   where o.code=#{data.code} or o.id = #{data.id}