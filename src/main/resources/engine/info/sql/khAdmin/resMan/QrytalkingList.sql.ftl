select * from (
SELECT
t.id,
t.title,
t.content as content,
t.community_id as comId,
d.`name` as comName,
t.create_by as createBy,
t.create_time as createTime
FROM t_talking t LEFT JOIN t_community c ON t.community_id=c.id
LEFT JOIN t_dict d on d.code=t.community_id and d.cata_code='t_talking.community_id'
where flag=0
) a