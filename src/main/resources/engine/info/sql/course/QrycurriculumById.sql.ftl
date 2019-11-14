select 
	id as curriculumId,
	name as curriculumName
	from t_curriculum
	where school_id = #{data.schoolId} and valid_flag = 0 and id != #{data.Id}

				