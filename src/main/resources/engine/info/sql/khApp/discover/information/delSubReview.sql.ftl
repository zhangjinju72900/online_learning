delete
from t_subject_review
where id = #{data.id} and review_by = #{data.session.userInfo.userId}