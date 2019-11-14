select * from (
	SELECT
		s.id as schoolId,
		s.name as schoolName
	FROM t_school s
	WHERE s.name = #{data.schoolName} and valid_flag = '0'
)a
