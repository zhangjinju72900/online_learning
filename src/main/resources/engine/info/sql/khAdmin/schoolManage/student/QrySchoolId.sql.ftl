	SELECT
	s.id as id,
	r.name as regionName
	from t_school s
	LEFT JOIN t_region r on r.id=s.region_id 
		where s.valid_flag = 0 and  s.name=#{data.schoolName};
