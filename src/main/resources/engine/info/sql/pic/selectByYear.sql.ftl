select * from (
    SELECT
    *
    FROM
    (
     SELECT
    tc.grade AS grade,
    count( tcuc.customer_user_id) AS userNum
    FROM
    t_customer_user_class tcuc
		LEFT JOIN t_class tc ON tcuc.class_id = tc.id
		GROUP BY
		tc.grade
    ) year_sum
    WHERE
    grade is not null
) a
