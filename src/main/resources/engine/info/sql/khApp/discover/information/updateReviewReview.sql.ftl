UPDATE t_information_review
SET review_count = (
	SELECT
		c
	FROM
		(
			SELECT
				count(1) AS c
			FROM
				t_information_review
			WHERE
				review_id = (
					SELECT
						review_id
					FROM
						t_information_review
					WHERE
						id =  #{data.id}
				)
			AND check_status = 1
		) tab1
)
WHERE
	id = (
		SELECT
			id
		FROM
			(
				SELECT
					review_id AS id
				FROM
					t_information_review
				WHERE
					id =  #{data.id}
			) tab1
	);
