select * from (
select
ta.id as id,
ta.school_id  as school,
ta.activity_id  as activityId,
s.name as name
from t_activity_join_school ta
LEFT JOIN t_activity  a on a.id=ta.activity_id
LEFT JOIN t_school s on s.id=ta.school_id
where ta.activity_id=#{data.id}

) a