UPDATE t_message_record SET read_flag = 1 , update_time = now(), update_by = #{data.session.userInfo.empId} WHERE send_type = #{data.sendType} and receiver_id=#{data.session.userInfo.empId}