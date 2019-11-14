update t_information_collect c,t_information i
    set c.collect_count = i.collect_count
WHERE c.information_id = #{data.id}

