select * from (
	SELECT
		p.id as id,
		p.pic_id as picId,
	FROM t_goods_pic p
	WHERE p.good_id = #{data.id}
) tab