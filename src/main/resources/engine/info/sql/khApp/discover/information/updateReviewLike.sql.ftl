	update  t_information_review
	set like_count = (select count(id) from t_information_like 
					  where  review_id = #{data.id})
	where id = #{data.id}