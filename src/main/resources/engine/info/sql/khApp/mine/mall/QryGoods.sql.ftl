select a.*,g.amount from (
	SELECT 
	tg.id as id,
	tg.name as name,
	tg.collect_count as collectCount,
	SUBSTRING_INDEX(GROUP_CONCAT(pic.pic_id  ORDER BY pic.id),',',1) as picId,
	case when GROUP_CONCAT(tgd.integral order by tgd.integral desc) is null then 0 else SUBSTRING_INDEX(GROUP_CONCAT(tgd.integral order by tgd.integral desc),',',1) end integral,
	case when GROUP_CONCAT(tgd.id order by tgd.integral desc) is null then 0 else SUBSTRING_INDEX(GROUP_CONCAT(tgd.id order by tgd.integral desc),',',1) end payId
	from t_goods tg
LEFT JOIN t_goods_pic pic on pic.good_id=tg.id
	left join  t_goods_pay_detail  tgd on tg.id=tgd.good_id
	where tg.sale_status=0 and tg.valid_flag = 0 GROUP BY tg.id
) a 
 JOIN t_goods_pay_detail g on g.id=a.payid
