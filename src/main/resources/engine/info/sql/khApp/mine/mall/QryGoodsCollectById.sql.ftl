	SELECT 
 id,
 good_id as goodId,
 collect_by as collectBy,
 collect_count as collectCount
 from t_goods_collect where collect_by = #{data.session.userInfo.userId} and good_id = #{data.goodId} and collect_count = 1