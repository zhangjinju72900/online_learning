SELECT *
FROM (
SELECT
 id AS id,
 question_id AS questionId,
 title AS title,
 content AS content,
 correct_flag AS correctFlag,
 create_time AS createTime,
 create_by AS createBy,
 update_time AS updateTime,
 update_by AS updateBy
FROM t_question_answer_options
where question_id=#{data.id}
) a