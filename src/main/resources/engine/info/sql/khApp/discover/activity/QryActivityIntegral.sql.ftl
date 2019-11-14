SELECT * FROM(
	SELECT join_integral
	FROM t_activity
	WHERE id=#{data.activityId}
)a