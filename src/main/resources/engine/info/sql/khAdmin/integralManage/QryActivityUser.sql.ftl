SELECT * FROM(
	SELECT id FROM t_activity_apply
	WHERE activity_id=#{data.activityId}   AND 
		  apply_by=#{data.userId}  AND 
		  valid_flag='0'
)a