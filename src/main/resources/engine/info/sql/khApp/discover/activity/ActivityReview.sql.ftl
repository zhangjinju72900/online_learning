INSERT INTO t_live_review(
      live_id,
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
      #{data.empId},
      now(),
      #{data.reviewEdit},
      0,
      0,
      now(),
      #{data.empId},
      now(),
      #{data.empId}
)


