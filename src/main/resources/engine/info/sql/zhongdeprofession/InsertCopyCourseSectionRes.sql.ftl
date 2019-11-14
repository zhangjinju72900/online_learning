INSERT INTO t_course_section_resources(
customer_resources_id,
course_id,
section_id,
label_id,
create_time,
create_by,
update_time,
update_by
)


SELECT
	cr.id,
#{data.courseId},
csr.section_id,
csr.label_id,
NOW(),
#{data.session.userInfo.userId},
NOW(),
#{data.session.userInfo.userId}
	FROM t_course_section_resources csr
	LEFT JOIN t_customer_resources cr
	ON csr.customer_resources_id=cr.id
	LEFT JOIN t_label l
	ON l.id=csr.label_id
		where cr.backup_type = '1' 
				and
			cr.valid_flag = '0' 
				and 
			cr.file_type != ''
				and
			csr.course_id=#{data.id}
				and 
			case when LOCATE('sec-', #{data.pid}) > 0 then  csr.section_id = substring(#{data.pid}, 5)
				else csr.section_id = 0 end