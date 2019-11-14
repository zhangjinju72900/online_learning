INSERT INTO t_section_label(section_id,label_id,create_time,create_by,update_by,update_time) 
SELECT 
	id,#{data.label},#{data.createTime},#{data.createBy},#{data.updateBy},#{data.updateTime}
FROM t_section 
WHERE course_id=(
		SELECT course_id
		FROM t_section
		WHERE id=#{data.courseorsectionId})