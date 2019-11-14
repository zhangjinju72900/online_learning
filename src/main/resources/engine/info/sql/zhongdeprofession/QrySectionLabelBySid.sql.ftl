select sl.id from t_course_section_resources sl left join t_section s on sl.section_id = s.id 
where sl.section_id = #{data.sectionId} and s.course_id = #{data.courseId}