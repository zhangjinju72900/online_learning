select * from (
SELECT
tg.id as id,
tg.name as name,
tg.collect_count as collectCount,
pic.pic_id as picId,
tg.content  AS content
from t_goods tg
) a