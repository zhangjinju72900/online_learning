SELECT *
FROM (
SELECT
id as id,
content AS content,
answer_thought AS answerThought
FROM t_question
) a
  