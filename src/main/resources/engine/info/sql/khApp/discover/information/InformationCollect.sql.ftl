update t_information i
    set collect_count = collect_count + 1
WHERE not EXISTS(
      SELECT collect_by
      FROM t_information_collect il
      WHERE collect_by = #{data.createBy} and information_id = #{data.id}
) and i.id = #{data.id}