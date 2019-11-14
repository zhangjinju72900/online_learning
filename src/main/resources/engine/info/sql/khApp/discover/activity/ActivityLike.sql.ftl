update t_activity a
    set like_count = like_count + 1
WHERE a.id = #{data.id}