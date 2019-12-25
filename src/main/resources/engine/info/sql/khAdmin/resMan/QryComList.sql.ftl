select * from (
select
name as text,
code as value
from t_dict
where cata_code='t_talking.community_id'
) a 