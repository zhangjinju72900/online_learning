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
      review_by,
      #{data.createBy},
      '2',
      now(),
      #{data.base_id},
      '',
      now(),
      #{data.createBy},
      now(),
      #{data.createBy}
FROM t_information_review
WHERE id = #{data.id}

