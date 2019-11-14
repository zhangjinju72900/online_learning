SELECT
	CONCAT("scorm", "-", cr.id) AS field,
	CASE
WHEN csr.course_resources_name = '' THEN
	cr.`name`
ELSE
	csr.course_resources_name
END AS title
FROM
	t_course_section_resources csr
LEFT JOIN t_customer_resources cr ON csr.customer_resources_id = cr.id
WHERE
	cr.resources_type = '14'
AND csr.course_id = #{data.courseId}
ORDER BY
	csr.id ASC
	