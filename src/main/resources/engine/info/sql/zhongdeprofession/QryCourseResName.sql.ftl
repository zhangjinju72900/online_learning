select * from (
SELECT
	csr.id as id,
	cr.name as name,
	case csr.course_resources_name when '' then cr.name 
	else csr.course_resources_name 
	end as courseResName
	FROM t_course_section_resources csr
	LEFT JOIN t_customer_resources cr
	ON csr.customer_resources_id=cr.id
	where csr.id = #{data.id} and cr.valid_flag = '0'
 ) a 