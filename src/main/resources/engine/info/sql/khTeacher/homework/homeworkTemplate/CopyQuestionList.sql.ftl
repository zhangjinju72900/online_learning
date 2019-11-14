INSERT into t_question (
	content,
question_classify_id,
question_type,
difficulty_level,
enable_status,
answer_thought,
valid_flag,
data_flag,
teacher_id,
create_time,
create_by,
update_time,
update_by,
uuid,
base_id
)

select 
 content,
-1,
question_type,
difficulty_level,
enable_status,
answer_thought,
valid_flag,
3,
teacher_id,
create_time,
create_by,
update_time,
update_by,
#{data.uuid},
#{data.questionId}
from t_question t where id=#{data.questionId}