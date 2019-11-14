SELECT taa.id,
tcu.name,
tcu.nickname,
(case
when tcu.sex = 0 then '男'
when tcu.sex = 1 then '女'
else '其他' end) sex,
ts.name as        school,
c.class as        class,
r.role  as        role
FROM t_activity_apply taa
LEFT JOIN t_customer_user tcu ON taa.apply_by = tcu.id
LEFT JOIN t_school ts ON tcu.school_id = ts.id
LEFT JOIN (SELECT tcuc.customer_user_id,
GROUP_CONCAT(tc.name) as class
FROM t_customer_user_class tcuc
LEFT JOIN t_class tc
ON tcuc.class_id = tc.id
GROUP BY tcuc.customer_user_id) c ON c.customer_user_id = tcu.id
LEFT JOIN (SELECT tcur.customer_user_id,
GROUP_CONCAT(tr.name) as role
FROM t_customer_user_role tcur
LEFT JOIN t_role tr ON tcur.role_id = tr.id
GROUP BY tcur.customer_user_id) r ON r.customer_user_id = tcu.id
WHERE activity_id = #{data.activityId}
