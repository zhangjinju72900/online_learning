SELECT * FROM(
select 
	id as id
	from t_activity_join
	where activity_id=#{data.activityId}
	and join_by=#{data.joinBy}
)a