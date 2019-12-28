select * from (
select
DISTINCT
	id as value,
	title as text
from t_activity a
where a.valid_flag = 0 and a.release_status = 1
) a