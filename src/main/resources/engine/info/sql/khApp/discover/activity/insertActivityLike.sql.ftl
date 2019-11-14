INSERT INTO t_live_like(live_id,like_by,like_time,create_time,create_by,update_time,update_by)
SELECT #{data.id},#{data.createBy},now(),now(),#{data.createBy},now(),#{data.createBy}
FROM dual
WHERE not EXISTS(
      SELECT like_by
      FROM t_live_like
      WHERE like_by = #{data.createBy} and live_id = #{data.id}
)

