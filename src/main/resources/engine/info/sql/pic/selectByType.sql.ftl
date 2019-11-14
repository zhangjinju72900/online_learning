select * from (
    SELECT
    *
    FROM
    (
     SELECT
    d.name as classType,
    count( tcuc.customer_user_id) AS userNum
    FROM
    t_customer_user_class tcuc
	LEFT JOIN t_class tc ON tcuc.class_id = tc.id
	left join t_dict d on d.code = tc.class_type and d.cata_code = 't_class.class_type'
		GROUP BY
		d.name
    ) year_sum
    WHERE
    classType is not null
) a
