select * from (
	SELECT
		cc.course_id as value,
		co.`name` as text
	FROM
		t_curriculum_course cc
	LEFT JOIN t_class c ON cc.curriculum_id = c.curriculum_id
	LEFT JOIN t_course co ON cc.course_id = co.id
	where c.id = #{data.classId}
 ) a 