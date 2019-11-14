insert into   t_notice_school
(
notice_id,
school_id,
class_id,
create_time,
create_by,
update_time,
update_by
)

select
 #{data.noticeId},
 s.id as schoolId,
 c.id as classId,
 now(),
 #{data.session.userInfo.userId}, 
 now(),
 #{data.session.userInfo.userId}
from t_school  s LEFT JOIN t_class c on c.school_id=s.id 
where c.id=#{data.className}
