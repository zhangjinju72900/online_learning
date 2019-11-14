select *
from (
select tacsr.id                     AS id,
tacsr.attend_class_record_id AS attendClassRecordId,
tacsr.student_id             AS studentId,
tacsr.valid_flag as validFlag,
case
when tacsr.valid_flag = 0
then '已签到'
when tacsr.valid_flag = 1
then '未签到'
end                        AS validFlagName,
tcu.name                     AS name,
tcu.file_id   as fileId,
ifnull(f.oss_url,'')     as ossUrl
from t_attend_class_sign_record tacsr
left join t_customer_user tcu on tacsr.student_id = tcu.id 
left join t_file_index f on tcu.file_id = f.id
where tacsr.attend_class_record_id = #{data.attendClassRecordId}
) a 
