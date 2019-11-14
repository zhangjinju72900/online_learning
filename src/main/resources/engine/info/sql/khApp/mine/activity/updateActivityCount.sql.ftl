update t_activity set join_count=join_count+1
where id=#{data.activityId}