select * from (
	SELECT 
	tg.id as id,
	tg.name as name,
	tg.collect_count as collectCount,
	pic.pic_id as picId,
	tg.content  as content
	from t_goods tg
	left join (select good_id, GROUP_CONCAT(pic_id) as pic_id from t_goods_pic where valid_flag = '0' GROUP BY good_id) pic on tg.id=pic.good_id
	where tg.id =67 and tg.sale_status=0
) a 