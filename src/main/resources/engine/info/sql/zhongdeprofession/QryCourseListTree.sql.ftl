select concat(pid,id), id, pid, text, '22' as courseId from (
	SELECT
		CONCAT('cou-',c.id) AS id,
		'' AS pid,
		CONCAT('课程-', c.name) AS text,
		c.show_order as showOrder,
		0 as defaultOrder
		
	FROM t_course c WHERE c.id=#{data.courseId} and c.valid_flag=0
	
	UNION ALL
	SELECT
		CONCAT('sec-',s.id) AS id,
		CONCAT('cou-',s.course_id) AS pid,
		CONCAT('小节-', s.name) AS text,
		s.show_order as showOrder,
		1 as defaultOrder
		
	FROM t_section s WHERE s.course_id=#{data.courseId} and s.valid_flag=0
	
	UNION ALL
	SELECT
		cl.label_id AS id,
	    CONCAT('cou-',cl.course_id) AS pid,
		CONCAT('标签-', l.name) AS text,
		l.show_order as showOrder,
		2 as defaultOrder
		
	FROM t_course_label cl 
	LEFT JOIN t_label l
	ON l.id=cl.label_id and l.valid_flag=0  where l.label_type = 0
	AND cl.course_id=#{data.courseId} 
	
	UNION ALL
	SELECT
		sl.label_id AS id,
		CONCAT('sec-',sl.section_id) AS pid,
		CONCAT('标签-', la.name) AS text,
		la.show_order as showOrder,
		3 as defaultOrder
		
	FROM (SELECT * FROM t_section_label WHERE section_id IN(
			SELECT id FROM t_section WHERE course_id=#{data.courseId} and valid_flag = '0'
		)) sl
	LEFT JOIN t_label la
	ON la.id=sl.label_id and la.valid_flag=0 where la.label_type = 1
	
 ) a 
 order by defaultOrder, showOrder