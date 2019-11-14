select * from (
	SELECT 
		cc.id as id,
		cc.subject_id as subId,
		cc.information_id as informationId, 
		c.title as title
		FROM t_subject_information cc 
		LEFT JOIN t_information c 
		ON cc.information_id = c.id
	WHERE cc.subject_id = #{data.id}
) tab
