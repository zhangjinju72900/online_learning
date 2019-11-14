select * from(
	SELECT
		p.id as id,
		i.filename as name,
		p.good_id as goodId,
		p.create_time as createTime,
		p.create_by as createBy,
		p.update_time as updateTime,
		p.update_by as updateBy,
		p.pic_id as picId,
		"" as remark
	FROM t_goods_pic p
	left join t_file_index i on p.pic_id = i.id
	WHERE p.good_id = #{data.id}
) a