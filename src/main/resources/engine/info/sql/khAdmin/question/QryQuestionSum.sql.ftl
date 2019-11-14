SELECT *
FROM (
SELECT
 count(*) as sum
FROM t_question
where valid_flag = 0 and question_classify_id = #{data.questionClassifyId} 
	and content = #{data.content} and question_type = #{data.questionType} and valid_flag=0
) a