update t_information i
    set i.collect_count = i.collect_count -1
WHERE i.id = #{data.id}