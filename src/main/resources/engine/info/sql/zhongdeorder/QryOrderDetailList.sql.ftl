select * from (
select id, integral, amount from t_goods_pay_detail where good_id in (
select good_id from t_order_detail where order_id = #{data.id})
 ) a 