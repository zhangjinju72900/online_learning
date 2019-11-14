select a.activity_type, i.release_by, a.integral, a.join_integral,a.id as activityId, 
(select count(1) from t_information where activity_id = i.activity_id and release_by = i.release_by) as count 
from t_information i left join t_activity a 
 on i.activity_id = a.id where i.id = #{data.id}