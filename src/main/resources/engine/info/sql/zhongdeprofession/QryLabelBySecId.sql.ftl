SELECT
	sl.*
		
	FROM (SELECT * FROM t_section_label WHERE section_id IN(
			SELECT id FROM t_section WHERe id=#{data.id} and  valid_flag = '0'
		)) sl
	LEFT JOIN t_label la
	ON la.id=sl.label_id and la.valid_flag=0 where la.label_type = 1