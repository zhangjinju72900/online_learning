select * from (
select
c.name as name,
c.id as id,
c.parent_id as parent,
c.create_time as createComTime,
c.create_by as createComBy
from t_community as c
) a