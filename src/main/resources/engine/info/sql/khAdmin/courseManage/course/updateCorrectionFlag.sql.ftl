update t_course_resources_error_recovery set valid_flag=1 ,update_time=#{data.updateTime} ,update_by=#{data.updateBy} 
where id=#{primaryFieldValue} ;
