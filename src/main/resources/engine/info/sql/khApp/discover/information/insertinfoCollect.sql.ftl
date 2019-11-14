INSERT INTO t_information_collect(
      information_id,
      collect_by,
      collect_time,
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
      SELECT collect_by
      FROM t_information_collect
      WHERE collect_by = #{data.createBy} and information_id = #{data.id}
)

