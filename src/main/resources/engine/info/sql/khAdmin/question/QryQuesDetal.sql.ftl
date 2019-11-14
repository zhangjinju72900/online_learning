SELECT *
FROM (
SELECT
id AS id,
enable_status AS enableStatus,
question_classify_id AS questionClassifyId,
content AS content,
question_type AS questionType,
difficulty_level AS difficultyLevel,
update_by AS updateBy,
update_time AS updateTime,
answer_thought AS answerThought
FROM t_question 
) a
  