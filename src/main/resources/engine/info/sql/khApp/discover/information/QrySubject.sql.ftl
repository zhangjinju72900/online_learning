select
  s.id,
  s.title,
  s.file_id as fileId,
  s.content,
  s.click_count as clickCount,
  s.review_count as reviewCount,
  s.recommend_flag as recommendFlag,
  s.create_time as createTime,
  s.release_by as releaseBy,
  s.release_time as releaseTime,
  s.create_by as createBy,
  s.update_time as updateTime,
  u.file_id as uFileId,
  s.update_by as updateBy
from t_subject s
left join t_customer_user u on s.release_by = u.id
where s.id = #{data.id}