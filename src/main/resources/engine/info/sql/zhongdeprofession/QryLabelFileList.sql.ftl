select * from (
SELECT
	csr.id as id,
	cr.name as name,
	cr.file_type as fileType,
	cr.file_path as filePath,
	cr.parent_id as orgId,
	cr.parent_id as parentId,
	cr.backup_type as backupType,
	cr.version_code as versionCode,
	cr.resources_type as resourcesType,
	csr.create_by as createBy,
	csr.create_time as createTime,
	csr.update_by as updateBy,
	csr.update_time as updateTime,
	case csr.course_resources_name when '' then cr.name 
	else csr.course_resources_name 
	end as courseResName
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
			l.id=#{data.treeId}
				and
			csr.course_id=#{data.courseId}
				and 
			case when LOCATE('sec-', #{data.pid}) > 0 then  csr.section_id = substring(#{data.pid}, 5)
				else csr.section_id = 0 end
 ) a 