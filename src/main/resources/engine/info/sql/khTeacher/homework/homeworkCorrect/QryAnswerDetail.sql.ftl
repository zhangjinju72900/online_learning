		SELECT DISTINCT
			a.id AS id,
			a.homework_answer_id AS homeworkAnswerId,
			a.content AS content,
			q.id AS questionId,
			q.content AS questionContent,
			f.ossKeys AS questionOssKeys,
			ans.ansOssUrls,
			ans.ansOssKeys
		FROM
			t_homework_detail_answer a
		LEFT JOIN t_question q ON a.question_id = q.id
		LEFT JOIN (
			SELECT
				q1.id AS id,
				group_concat(f1.oss_key) AS ossKeys
			FROM
				t_question q1
			LEFT JOIN t_question_file f1 ON q1.id = f1.question_id
			GROUP BY
				f1.question_id
		) f ON f.id = q.id
		LEFT JOIN (
			SELECT
				af.homework_detail_answer_id,
				group_concat(af.oss_url) AS ansOssUrls,
				group_concat(af.oss_key) AS ansOssKeys
			FROM
				t_homework_detail_subjective_answer_file af
			GROUP BY
				af.homework_detail_answer_id
		) ans ON ans.homework_detail_answer_id = a.id
		WHERE
			a.homework_answer_id = #{data.homeworkAnswerId} AND q.valid_flag = 0
		ORDER BY
			q.id ASC