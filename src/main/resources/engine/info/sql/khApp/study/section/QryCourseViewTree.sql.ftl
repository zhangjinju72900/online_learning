select concat(pid,id), id, pid, text, type, typeName, remark, fileType, csrId, resourcesType,vid from (
	select 
		0 as csrId,
		CONCAT('course-', s.id) as id, 
		'' as pid,
		'course' as type,
		s.`name` AS text,
		'课程' as typeName,
		s.remark,
		'' as fileType,
		0 as show_order,
		0 as resourcesType,
		'' as vid
	from t_course s where id = #{data.courseId}
	UNION ALL
	SELECT
		0 as csrId,
		CONCAT('cou-section-', s.id) AS id,
		CONCAT('course-', s.course_id) AS pid,
		'course-section' as type,
		s.`name` AS text,
		'课程-小节' as typeName,
		s.remark as remark,
		'' as fileType,
		s.show_order,
		0 as resourcesType,
		'' as vid
	FROM t_section s WHERE s.course_id = #{data.courseId} and valid_flag = '0'
	UNION ALL
	SELECT
		0 as csrId,
		CONCAT('cou-label-', cl.label_id) AS id,
	  	CONCAT('course-', cl.course_id) AS pid,
		'course-label' as type,
		tab1.`name` AS text,
		'课程-标签' as typeName,
		'' as remark,
		'' as fileType,
		tab1.show_order,
		0 as resourcesType,
		'' as vid
	FROM t_course_label cl 
			JOIN (
			SELECT
				DISTINCT
				l.id,
				l.`name`,
				l.show_order
			FROM
				t_label l
			JOIN t_label_role lr ON l.id = lr.label_id
			WHERE
				l.label_type = 0
			AND FIND_IN_SET(lr.role_id, #{data.session.userInfo.version}) != 0
		) tab1 ON tab1.id = cl.label_id
	AND cl.course_id = #{data.courseId}
	UNION ALL
	SELECT
		0 as csrId,
		CONCAT('cou-section-label-',sl.section_id, "-", sl.label_id) AS id,
		CONCAT('cou-section-', sl.section_id) AS pid,
		'course-section-label' as type,
		tab1.`name` AS text,
		'课程-小节-标签' as typeName,
		'' as remark,
		'' as fileType,
		tab1.show_order,
		0 as resourcesType,
		'' as vid
	FROM (SELECT * FROM t_section_label WHERE section_id IN(
			SELECT id FROM t_section WHERE course_id = #{data.courseId} and valid_flag = '0'
		)) sl
		JOIN (
		SELECT
			DISTINCT
			l.id,
			l.`name`,
			l.show_order
		FROM
			t_label l
		JOIN t_label_role lr ON l.id = lr.label_id
		WHERE
			l.label_type = 1
		AND FIND_IN_SET(lr.role_id, #{data.session.userInfo.version}) != 0
	) tab1 ON tab1.id = sl.label_id 
	
	UNION ALL
	
	select 
		csr.id as csrId,
		csr.customer_resources_id as id,
		CONCAT('cou-label-', csr.label_id) AS pid,
		'course-label-resource' as type,
		case csr.course_resources_name when '' then cr.name 
		else csr.course_resources_name 
		end as text,
		'课程-标签-资源' as typeName,
		'' as remark,
		cr.file_type as fileType,
		999 as show_order,
		cr.resources_type as resourcesType,
		cr.oss_key as vid
		from t_course_section_resources csr
		LEFT JOIN t_customer_resources cr on csr.customer_resources_id = cr.id
		where csr.section_id = '0' and cr.valid_flag = '0' and csr.course_id = #{data.courseId}
	UNION ALL
	select 
		csr.id as csrId,
		csr.customer_resources_id as id,
		CONCAT('cou-section-label-', csr.section_id, '-', csr.label_id) AS pid,
		'course-section-label-resource' as type,
		case csr.course_resources_name when '' then cr.name 
		else csr.course_resources_name 
		end as text,
		'课程-小节-标签-资源' as typeName,
		'' as remark,
		cr.file_type as fileType,
		999 as show_order,
		cr.resources_type as resourcesType,
		cr.oss_key as vid
		from t_course_section_resources csr
		LEFT JOIN t_customer_resources cr on csr.customer_resources_id = cr.id
		where csr.section_id != '0' and cr.valid_flag = '0' and csr.course_id = #{data.courseId}
		
		union ALL
select * from (select
	  ts.file_id as csrId,
		ts.id as id, 
		CONCAT('assist-',ts.section_id) as pid,
		'assist' as type,
		ts.`name` AS text,
		'教辅资料' as typeName,
		 '' as remark,
		f.file_type as fileType,
		0 as show_order,
		0 as resourcesType,
		ts.oss_key as vid
from t_teaching_assist  ts 
LEFT JOIN t_file_index f on f.id=ts.file_id
 where ts.course_id= #{data.courseId} and ts.visible_flag=0 and ts.valid_flag=0

union ALL

select
	  0 as csrId,
		CONCAT('assist-',section_id) as id, 
		CONCAT('cou-section-',section_id) as pid,
		'assist' as type,
		'教辅资料' AS text,
		'教辅资料' as typeName,
		 '' as remark,
		'' as fileType,
		0 as show_order,
		0 as resourcesType,
		oss_key as vid
from t_teaching_assist GROUP BY section_id)dd
 ) a ORDER BY show_order, id 