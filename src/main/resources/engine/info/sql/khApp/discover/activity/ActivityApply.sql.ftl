update t_activity a
    set apply_count = apply_count + 1
WHERE not EXISTS(
      SELECT apply_by
      FROM t_activity_apply ta
      WHERE apply_by = #{data.createBy} and ta.activity_id = #{data.id}
) and a.id = #{data.id}