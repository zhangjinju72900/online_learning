update t_subject s
    set click_count = click_count + 1
WHERE s.id = #{data.id}