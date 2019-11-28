select * from (
select concat(parent_id,id),
id as id,
CASE
WHEN parent_id = 0 THEN
''
ELSE
parent_id
END AS pid,
name as text,
'closed' as state
from t_community
ORDER BY concat(pid,id)
) a