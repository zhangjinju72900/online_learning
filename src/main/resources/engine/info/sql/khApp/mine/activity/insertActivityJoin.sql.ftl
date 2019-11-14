insert into t_activity_join(activity_id,join_by,join_time,create_time,create_by)
values(#{data.activityId},#{data.joinBy},now(),now(),#{data.createBy})