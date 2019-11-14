
select * from(
	select 
	ir.id as id,
	ir.information_id as infoId,
	case il.like_by when #{data.createBy} then 'y'
	else 'n'
	end  as likeBy
	from t_information_review ir
	left join (select DISTINCT review_id, like_by from t_information_like where review_id = #{data.id} and like_by = #{data.createBy}) il
	on ir.id = il.review_id where ir.id = #{data.id} 
)a
