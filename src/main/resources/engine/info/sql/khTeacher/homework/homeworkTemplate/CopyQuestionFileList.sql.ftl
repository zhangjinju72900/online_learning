insert t_question_file (
question_id,
file_id,
oss_key,
bucket_name,
create_time,
create_by,
update_time,
update_by
)

select 
#{data.qId},
file_id,
oss_key,
bucket_name,
create_time,
create_by,
update_time,
update_by
from t_question_file f where f.question_id=#{data.questionId}