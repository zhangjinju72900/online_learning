select COUNT(id)
from t_information_review
where information_id = #{data.id} and check_status = 1