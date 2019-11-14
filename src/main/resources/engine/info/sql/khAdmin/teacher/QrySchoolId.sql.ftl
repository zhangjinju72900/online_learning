select * from(
	SELECT
	id
	from t_school
	where valid_flag = 0 and #{data.schoolName} = name
) a