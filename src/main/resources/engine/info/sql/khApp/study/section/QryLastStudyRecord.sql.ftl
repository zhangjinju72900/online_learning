select percent_score as percentScore from t_course_resources_score crs join t_course_section_resources r on crs.course_id = r.course_id
and crs.section_id = r.section_id and crs.label_id = r.label_id and crs.customer_resources_id = r.customer_resources_id
where r.id = #{data.csrId} and customer_user_id = #{data.userId} order by crs.id desc LIMIT 1