INSERT INTO t_activity_forward_record(
      activity_id,
      forward_time,
      forward_type,
      forward_by,
      create_time,
      create_by,
      update_time,
      update_by
)
value (
      #{data.id},
      now(),
      #{data.forwardType},
      #{data.createBy},
      now(),
      #{data.createBy},
      now(),
      #{data.createBy}
)


