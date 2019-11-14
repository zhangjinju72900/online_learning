delete
from t_live_review
where id = #{data.id} and review_by = #{data.session.userInfo.userId}