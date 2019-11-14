INSERT INTO t_message_record(
      receiver_id,
      sender_id,
      send_type,
      send_time,
      base_id,
      remark,
      create_time,
      create_by,
      update_time,
      update_by
)
SELECT
      #{data.createBy},
      originator,
      '10',
      now(),
      id,
      '',
      now(),
      #{data.createBy},
      now(),
      #{data.createBy}
FROM t_activity
WHERE id = (select activity_id from t_activity_apply where id= #{data.base_id})

