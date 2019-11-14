INSERT INTO t_subject_click(
      subject_id,
      click_by,
      click_time,
      create_time,
      create_by,
      update_time,
      update_by
)
value(
      #{data.id},
      #{data.session.userInfo.userId},
      now(),
      now(),
      #{data.session.userInfo.userId},
      now(),
      #{data.session.userInfo.userId}
)
