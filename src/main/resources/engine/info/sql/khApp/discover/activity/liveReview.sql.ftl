select COUNT(id)
from t_live_review
where live_id = #{data.id} and check_status = 1