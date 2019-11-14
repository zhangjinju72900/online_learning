select * from (
	SELECT
		cc.curriculum_id as curriculumId,
		cc.class_id as classId,
		c.name as className,
		c.grade as gradeName
	FROM t_class_curriculum cc
	LEFT JOIN t_class c
	ON cc.class_id = c.id
)a
