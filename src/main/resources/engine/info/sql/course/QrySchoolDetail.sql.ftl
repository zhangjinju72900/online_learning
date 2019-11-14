select * from (
	SELECT 
		c.id as id,
		c.school_id as schoolId,
		s.name as schoolName
		FROM t_class_curriculum c 
		LEFT JOIN t_school s 
		ON c.school_id = s.id
	WHERE c.curriculum_id = #{data.id}
) tab
