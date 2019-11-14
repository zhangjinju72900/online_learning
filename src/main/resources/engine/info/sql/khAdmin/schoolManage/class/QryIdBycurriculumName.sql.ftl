select id from t_curriculum 
where name = #{data.name} and school_id = #{data.schoolId} and valid_flag = 0;
