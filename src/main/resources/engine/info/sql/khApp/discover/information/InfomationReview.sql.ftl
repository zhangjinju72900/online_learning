INSERT INTO t_information_review(
      information_id,
      review_by,
      review_time,
      content,
      check_status,
      valid_flag,
      create_time,
      create_by,
      update_time,
      update_by
)
value (
      #{data.id},
      #{data.createBy},
      now(),
      #{data.reviewEdit},
      0,
      0,
      now(),
      #{data.createBy},
      now(),
      #{data.createBy}
)


