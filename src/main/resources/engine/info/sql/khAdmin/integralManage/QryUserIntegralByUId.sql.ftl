select aa.id from t_customer_user_integral_record aa 
 where aa.customer_user_id = #{data.userId} and aa.change_type = #{data.changeType} and aa.base_order_type = #{data.changeType}