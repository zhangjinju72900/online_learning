select * from(
	SELECT
	id
	from t_class
	where valid_flag = 0 and #{data.className} = name and #{data.grade} = grade and #{data.schoolId} = school_id
) a