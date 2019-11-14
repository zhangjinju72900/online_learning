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
sectionId,
labelId,
resourcesType,
filePath
from (
	
	SELECT 
			0                                                             as csrId,
			CONCAT('cou-sec-la-', sl.section_id, '-', sl.label_id) AS id,
			''                         AS pid,
			'label'                                       				  as type,
			tab1.`name`                                                   AS text,
			'课程-小节-标签'                                                    as typeName,
			''                                                            as remark,
			''                                                            as fileType,
			tab1.show_order,
			'' as ossKey,
			'' as bucketName,
			sl.section_id as sectionId,
			sl.label_id as labelId,
			0 as resourcesType,
			"" as filePath
		FROM (SELECT *
		FROM t_section_label
		WHERE section_id = #{data.sectionId}) sl
		JOIN (
		SELECT DISTINCT l.id,
		l.`name`,
		l.show_order
		FROM t_label l
		JOIN t_label_role lr ON l.id = lr.label_id
		WHERE l.label_type = 1
		AND FIND_IN_SET(lr.role_id, #{data.session.userInfo.version}) != 0
		) tab1 ON tab1.id = sl.label_id

	UNION ALL
	
		select  csr.id                                                          as csrId,
			csr.customer_resources_id					                    as id,
			CONCAT('cou-sec-la-', csr.section_id, '-', csr.label_id) AS pid,
			'resource'                                 						as type,
			case csr.course_resources_name
			when '' then cr.name
			else csr.course_resources_name
			end                                                           as text,
			'课程-小节-标签-资源'                                                   as typeName,
			''                                                              as remark,
			cr.file_type                                                    as fileType,
			999                                                             as show_order,
			case when cr.oss_key is null or cr.oss_key='' then f.oss_key else cr.oss_key end ossKey,
			case when cr.bucket_name is null or cr.bucket_name='' then SUBSTR(f.oss_url,8,11) else  cr.bucket_name end as bucketName,
			csr.section_id as sectionId,
			csr.label_id as labelId,
			cr.resources_type as resourcesType,
			cr.file_path as filePath
		from t_course_section_resources csr
		LEFT JOIN t_customer_resources cr on csr.customer_resources_id = cr.id
		LEFT JOIN t_file_index f on f.id=cr.file_id
		where csr.section_id = #{data.sectionId}
		and cr.valid_flag = '0'
) a
ORDER BY show_order, id