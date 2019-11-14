select * from (
SELECT
	crs.course_id as id,
	crs.customer_resources_id as customerResourcesId,
	cr.`name` as courseName,
	crs.score
FROM
	t_course_resources_score crs
LEFT JOIN t_customer_resources cr ON crs.customer_resources_id = cr.id
order by crs.update_time desc
)a