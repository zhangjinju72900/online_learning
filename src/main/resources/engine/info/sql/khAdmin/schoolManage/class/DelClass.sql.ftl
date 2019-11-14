UPDATE t_class set valid_flag = 1, update_by = #{data.session.userInfo.empId}, update_time = now() WHERE id = #{data.id};
delete from t_customer_user_class where class_id = #{data.id};
