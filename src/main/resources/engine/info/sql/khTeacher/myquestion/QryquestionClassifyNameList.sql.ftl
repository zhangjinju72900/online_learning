SELECT *
FROM (
SELECT
		id AS value,
		name AS text
FROM t_question_classify where valid_flag = 0 
		
) a