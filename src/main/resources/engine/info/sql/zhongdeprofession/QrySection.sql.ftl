SELECT * FROM(
	SELECT
		s.id AS id,
		s.name AS name,
		s.course_id AS courseId,
		s.remark AS remark,
		s.show_order AS showOrder,
		s.valid_flag AS validFlag,
		s.create_by	AS createBy,
		s.create_time AS createTime,
		s.update_by	AS	updateBy,
		s.update_time AS updateTime,
		csr.customer_resources_id AS sourceId,
		cr.name AS sourceName
	FROM t_section s
	LEFT JOIN t_course_section_resources csr
	ON s.id=csr.section_id
	LEFT JOIN t_customer_resources cr
	ON cr.id=csr.customer_resources_id
	where s.valid_flag = '0'
)a