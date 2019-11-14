SELECT *
FROM (
SELECT
id,
name
FROM t_question_classify where id = #{data.id}
) a
  