delete
from t_goods_collect
where good_id = #{data.id} and collect_by = #{data.createBy}