select * from(
SELECT
DISTINCT
ta.id AS id,
ta.role_id  as roleId,
ta.activity_id as activityId
FROM
t_activity_join_role ta
where ta.activity_id=(select a.id from t_activity a
where a.id=#{data.activity}
)
) a