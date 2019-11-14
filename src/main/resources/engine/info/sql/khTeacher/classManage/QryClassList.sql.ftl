select * from (
SELECT 
	tcuc.id AS id,
	tcuc.class_id AS classId,
	tcuc.customer_user_id AS teacherId,
	CONCAT(tc.grade,"级",tc.name) AS className,
	tc.grade AS grade,
	tc.class_type AS classType 
FROM t_customer_user_class tcuc 
LEFT JOIN t_class tc ON tc.id = tcuc.class_id
 ) a