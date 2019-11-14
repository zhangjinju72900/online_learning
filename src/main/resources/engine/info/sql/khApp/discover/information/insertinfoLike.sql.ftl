INSERT INTO t_information_like(
      information_id,
      like_by,
      like_time,
      create_time,
      create_by,
      update_time,
      update_by
)
SELECT
      #{data.id},
      #{data.createBy},
      now(),
      now(),
      #{data.createBy},
      now(),
      #{data.createBy}
FROM dual
WHERE not EXISTS(
      SELECT like_by
      FROM t_information_like
      WHERE like_by = #{data.createBy} and information_id = #{data.id}
)

