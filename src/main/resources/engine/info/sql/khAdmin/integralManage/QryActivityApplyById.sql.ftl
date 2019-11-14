select aa.id, aa.activity_id, aa.apply_by, a.activity_type, a.integral, a.join_integral from t_activity_apply aa left join t_activity a
on aa.activity_id = a.id
 where aa.id = #{data.id}