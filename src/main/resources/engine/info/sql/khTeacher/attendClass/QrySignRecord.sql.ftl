select id
from t_attend_class_sign_record
where valid_flag = 0
and attend_class_record_id = #{data.attendClassRecordId}