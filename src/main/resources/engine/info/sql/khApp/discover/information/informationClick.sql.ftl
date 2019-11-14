update t_information i
    set click_count = click_count + 1
WHERE i.id = #{data.id} and i.source = 1