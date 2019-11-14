update t_activity a
    set review_count =review_count + 1
WHERE a.id = #{data.id}