select * from (
select
DISTINCT
id as value,
name as text
from t_community
where id!=1 and flag=0
) a