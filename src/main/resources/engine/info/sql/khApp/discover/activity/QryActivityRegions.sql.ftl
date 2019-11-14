select * from (
select region_id as regionId,
				r.schoolId,
				r.classId
	from t_activity_join_region r
	where activity_id=#{data.activityId}
) a
