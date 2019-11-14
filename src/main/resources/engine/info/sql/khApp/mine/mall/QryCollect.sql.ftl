select 
id 
from t_goods_collect
where collect_by=#{data.userId} and good_id=#{data.goodId}