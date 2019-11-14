update t_goods g set g.collect_count =collect_count-1,update_time=now(),update_by=#{data.session.userInfo.userId}
WHERE EXISTS(
      SELECT collect_by
      FROM t_goods_collect gc
      WHERE gc.collect_by = #{data.session.userInfo.userId} and gc.good_id = #{data.eq_goodId} and collect_count = 1
) and g.id = #{data.eq_goodId}
