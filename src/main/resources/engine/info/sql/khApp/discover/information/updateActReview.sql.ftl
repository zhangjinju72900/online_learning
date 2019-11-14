update t_activity i
set review_count = (SELECT count(*)
                    FROM t_live_review
                    WHERE live_id = (select live_id from t_live_review where id = #{data.id})
                      and check_status = 1)
where id = (select live_id from t_live_review where id = #{data.id})