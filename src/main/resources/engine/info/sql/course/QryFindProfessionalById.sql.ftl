select * from (
	SELECT
		p.id as value
		p.name as text
	FROM t_professional p
	WHERE p.id = #{data.id}
) tab