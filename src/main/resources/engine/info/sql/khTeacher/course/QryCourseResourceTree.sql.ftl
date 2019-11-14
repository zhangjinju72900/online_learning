select concat(pid, id),
id,
pid,
text,
type,
typeName,
remark,
fileType,
csrId,
ossKey,
bucketName,
resourcesType,
filePath
from (
	SELECT 
		0                                 as csrId,
		CONCAT('cou-label-', cl.label_id) AS id,
		''   AS pid,
		'course-label'                    as type,
		tab1.`name`                       AS text,
		'课程-标签'                           as typeName,
		''                                as remark,
		''                                as fileType,
		tab1.show_order,
		'' as ossKey,
		'' as bucketName,
		0 as resourcesType,
		0 as filePath
	FROM t_course_label cl
	JOIN (
	SELECT DISTINCT l.id,
	l.`name`,
	l.show_order
	FROM t_label l
	JOIN t_label_role lr ON l.id = lr.label_id
	WHERE l.label_type = 0 and l.valid_flag = 0
	AND FIND_IN_SET(lr.role_id, #{data.session.userInfo.version}) != 0
	) tab1 ON tab1.id = cl.label_id
	AND cl.course_id = #{data.courseId}

UNION ALL

	select 
		csr.id                             as csrId,
		csr.customer_resources_id          as id,
		CONCAT('cou-label-', csr.label_id) AS pid,
		'course-label-resource'            as type,
		case csr.course_resources_name
		when '' then cr.name
		else csr.course_resources_name
		end                              as text,
		'课程-标签-资源'                         as typeName,
		''                                 as remark,
		cr.file_type                       as fileType,
		999                                as show_order,
		cr.oss_key as ossKey,
		cr.bucket_name as bucketName,
		cr.resources_type as resourcesType,
		cr.file_path as filePath
	from t_course_section_resources csr
		LEFT JOIN t_customer_resources cr on csr.customer_resources_id = cr.id
		where csr.section_id = '0'
		and cr.valid_flag = '0'
		and csr.course_id = #{data.courseId}) a
ORDER BY show_order, id