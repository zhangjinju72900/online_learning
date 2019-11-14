INSERT INTO `t_goods_collect`
(good_id, collect_by, collect_time, collect_count, create_time, create_by)
select 
	#{data.eq_goodId}, #{data.session.userInfo.userId}, now(), 1, now(), #{data.session.userInfo.userId}
from dual WHERE not EXISTS(
      SELECT collect_by
      FROM t_goods_collect gc
      WHERE gc.collect_by = #{data.session.userInfo.userId} and gc.good_id = #{data.eq_goodId}
)