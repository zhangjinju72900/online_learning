update t_banner set pic_id = #{data.fileId}, show_order = #{data.showorder}, 
banner_type = #{data.type}, update_by = #{data.updateby}, update_time = now() where id = #{data.id} 