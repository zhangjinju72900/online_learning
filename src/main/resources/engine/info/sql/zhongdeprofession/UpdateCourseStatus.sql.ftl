UPDATE t_course SET visible_flag=(case when visible_flag=0 THEN 1 ELSE 0 END)
WHERE id=#{data.id}