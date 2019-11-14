SELECT *
FROM (
SELECT
		code AS value,
		name AS text
FROM t_dict where cata_code = 't_homework_difficulty_level'		
) a