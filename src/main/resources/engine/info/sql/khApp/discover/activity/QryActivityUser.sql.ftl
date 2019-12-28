select * from(
SELECT
DISTINCT
tu.id AS id,
tu.name as applyName,
tu.integral  as  integral,
tcur.role_id as roleId
FROM
t_customer_user tu
LEFT JOIN t_customer_user_role tcur  on tcur.customer_user_id=tu.id
where tu.name=#{data.applyBy}
) a