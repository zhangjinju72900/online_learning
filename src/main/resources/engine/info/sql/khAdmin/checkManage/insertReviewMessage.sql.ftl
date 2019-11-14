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
      release_by,
      (select review_by from t_information_review where id= #{data.base_id}),
      '1',
      now(),
      #{data.base_id},
      '',
      now(),
      #{data.session.userInfo.userId},
      now(),
      #{data.session.userInfo.userId}
FROM t_information
WHERE id = (select information_id from t_information_review where id= #{data.base_id})

