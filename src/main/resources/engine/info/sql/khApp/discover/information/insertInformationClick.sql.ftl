INSERT INTO t_information_click(
      information_id,
      click_by,
      click_time,
      create_time,
      create_by,
      update_time,
      update_by
)
select
      #{data.id},
      #{data.session.userInfo.userId},
      now(),
      now(),
      #{data.session.userInfo.userId},
      now(),
      #{data.session.userInfo.userId}
from t_information i
WHERE  id = #{data.id}
