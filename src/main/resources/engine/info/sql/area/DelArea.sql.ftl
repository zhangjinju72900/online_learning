UPDATE t_region set valid_flag = 1, update_by = #{data.session.userInfo.empId}, update_time = now()
WHERE id = #{data.id}