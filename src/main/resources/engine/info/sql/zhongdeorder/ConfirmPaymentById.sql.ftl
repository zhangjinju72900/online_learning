select g.id as goodId, g.name as goodName,o.order_status, od.quantity from t_goods g join t_order_detail od on g.id = od.good_id LEFT JOIN t_order o ON od.order_id = o.id
where od.order_id = #{data.id}
  and g.sale_status = 0 and g.valid_flag = 0;