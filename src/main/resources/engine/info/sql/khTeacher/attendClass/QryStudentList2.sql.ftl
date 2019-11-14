select *
from (
select
tacsr.id, 
tacsr.attend_class_record_id AS attendClassRecordId,
tacsr.student_id  AS studentId,
case
when tacsr.valid_flag = 0
then '已签到'
when tacsr.valid_flag = 1
then '未签到'
end AS validFlag,
tcu.name AS name,
tacsr.create_time As createTime,
tacsr.update_time As updateTime
from t_attend_class_sign_record tacsr
left join t_customer_user tcu on tacsr.student_id = tcu.id 
left join t_file_index f on tcu.file_id = f.id
) a