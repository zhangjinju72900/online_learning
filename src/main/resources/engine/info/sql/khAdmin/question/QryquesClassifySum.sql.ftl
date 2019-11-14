SELECT *
FROM (
SELECT
 count(*) as sum
FROM t_question_classify
where valid_flag = 0 and name = #{data.name} 
) a