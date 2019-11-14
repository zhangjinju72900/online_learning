DELETE FROM t_section_label WHERE section_id IN (
	SELECT id FROM t_section WHERE course_id =(
		SELECT course_id FROM t_section WHERE id=#{data.sectionId}
	)
) and label_id=#{data.labelId}