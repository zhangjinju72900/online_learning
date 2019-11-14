SELECT *
FROM (
SELECT
id
FROM t_question where question_classify_id = #{data.questionClassifyId} and content = #{data.content}
) a