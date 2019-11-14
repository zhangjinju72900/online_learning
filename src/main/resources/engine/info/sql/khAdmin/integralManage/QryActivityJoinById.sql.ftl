select aa.id, aa.activity_id, a.activity_type, aa.join_by, a.integral, a.join_integral from t_activity_join aa left join t_activity a
on aa.activity_id = a.id
 where aa.id = #{data.id}