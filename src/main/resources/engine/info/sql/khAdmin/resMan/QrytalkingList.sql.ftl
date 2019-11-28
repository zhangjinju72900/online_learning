select * from (
SELECT
t.id,
t.title,
t.community_id as comId,
t.create_by as createBy,
t.create_time as createTime
FROM t_talking t LEFT JOIN t_community c ON t.community_id=c.id
where flag=0
) a