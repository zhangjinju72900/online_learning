select g.id as goodId, g.name as goodName, od.quantity from t_goods g join t_order_detail od on g.id = od.good_id
 where od.order_id = #{data.id} 
and g.sale_status = 0 and g.valid_flag = 0;