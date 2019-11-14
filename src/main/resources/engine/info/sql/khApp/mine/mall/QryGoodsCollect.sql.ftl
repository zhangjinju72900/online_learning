select * from (
	SELECT 
 id,
 good_id as goodId,
 collect_by as collectBy,
 collect_count as collectCount
 from t_goods_collect
) a 
