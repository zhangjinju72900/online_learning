SELECT *
FROM (
SELECT
		name
FROM t_question_classify where id = #{data.questionClassifyId}
		
) a