	insert  into t_information_review(
	review_id,
	information_id,
	review_by,
	review_time,
	content,
	create_by,
	create_time,
	update_time,
	update_by
	)
	values(
	#{data.reviewId},
	#{data.infoId},
	#{data.createBy},
	NOW(),
	#{data.content},
	#{data.createBy},
	NOW(),
	NOW(),
	#{data.createBy}
	)
	