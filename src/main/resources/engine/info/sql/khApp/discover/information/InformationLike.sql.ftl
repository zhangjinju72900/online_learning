update t_information i
    set like_count = like_count + 1
WHERE not EXISTS(
      SELECT like_by
      FROM t_information_like il
      WHERE like_by = #{data.createBy} and information_id = #{data.id} and review_id = 0
) and i.id = #{data.id}