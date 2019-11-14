INSERT INTO t_activity_apply(activity_id,apply_by,apply_time,apply_type,valid_flag,create_time,create_by,update_time,update_by)
SELECT #{data.id},#{data.createBy},now(),0,0,now(),#{data.createBy},now(),#{data.createBy}
FROM dual
WHERE not EXISTS(
      SELECT apply_by
      FROM t_activity_apply
      WHERE apply_by = #{data.createBy} and activity_id = #{data.id}
)

