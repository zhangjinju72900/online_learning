INSERT INTO `t_notice_file` (
`notice_id`,
`file_id`,
`oss_key`,
`bucket_name`,
`create_time`,
`create_by`,
`update_time`,
`update_by`
)
select 
#{data.noticeId},
#{data.fileId},
f.oss_key,
f.oss_url,
 now(),
 #{data.session.userInfo.userId},
 now(),
 #{data.session.userInfo.userId}
from t_file_index f where f.id=#{data.fileId}
