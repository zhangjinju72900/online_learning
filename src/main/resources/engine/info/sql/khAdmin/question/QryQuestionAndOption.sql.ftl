SELECT *
FROM (
SELECT
 id AS id,
 content AS content,
 question_classify_id AS questionClassifyId,
 question_type AS questionType,
 difficulty_level AS difficultyLevel,
 enable_status AS enableStatus,
 answer_thought AS answerThought,
 valid_flag AS validFlag,
 data_flag AS dataFlag,
 teacher_id AS teacherId,
 create_time AS createTime,
 create_by AS createBy,
 update_time AS updateTime,
 update_by AS updateBy
FROM t_question where id=#{data.questionId}
) a