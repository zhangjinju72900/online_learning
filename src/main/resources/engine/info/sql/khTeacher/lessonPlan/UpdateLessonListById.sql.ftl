update t_lesson_plan set valid_flag = '1', update_time = now(), update_by = #{data.session.userInfo.empId} where id = #{data.id}